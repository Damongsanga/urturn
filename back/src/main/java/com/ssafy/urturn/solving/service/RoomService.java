package com.ssafy.urturn.solving.service;

import static com.ssafy.urturn.global.RequestLockType.*;

import com.ssafy.urturn.global.RequestLockService;
import com.ssafy.urturn.global.RequestLockType;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.service.GradingService;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.solving.cache.cacheDatas;
import com.ssafy.urturn.solving.dto.*;
import com.ssafy.urturn.solving.temp.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final MemberService memberService;
    private final GradingService gradingService;
    private final ProblemService problemService;
    private final cacheDatas cachedatas;
    private final ReentrantLock lock;

    private final RequestLockService requestLockService;


    /**
    방생성
     */
    @Transactional
    public roomInfoResponse createRoom(Long userId){
        // 방 ID
        String roomId= UUID.randomUUID().toString();
        // 입장코드
        String entryCode=UUID.randomUUID().toString().substring(0,6);

        // 방 정보 DTO 생성
        RoomInfoDto roominfodto=new RoomInfoDto();
        roominfodto.setManagerId(userId);
        roominfodto.setRoomStatus(RoomStatus.WAITING);

        // 초대코드 키로 방 ID 캐시
        cachedatas.cacheRoomId(entryCode,roomId);

        // 방 ID 키로 방정보 캐시
        cachedatas.cacheroomInfoDto(roomId,roominfodto);

        return new roomInfoResponse(roomId, entryCode, true);

    }

    public UserInfoResponse getUserInfo(Long myUserId,Long relativeUserId){
        return memberService.getMemberInfo(myUserId,relativeUserId);
    }

    public String canEnterRoom(String entryCode) {
        // 캐시된 방 ID 가져오기
        String roomId = cachedatas.cacheRoomId(entryCode);
        if (roomId == null) {
            throw new RuntimeException("해당 방이 존재하지 않습니다.");
        }

        // 방 정보 가져오기
        RoomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
        if (roomInfo == null) {
            throw new RuntimeException("방 정보를 가져올 수 없습니다.");
        }

        // 방 상태 확인
        if (roomInfo.getRoomStatus() != RoomStatus.WAITING) {
            throw new RuntimeException("해당 방은 현재 " + roomInfo.getRoomStatus()+ " 상태입니다.");
        }

        // 참여자 ID 설정
        roomInfo.setParticipantId(MemberUtil.getMemberId());

        // 방 정보 업데이트
        cachedatas.cacheroomInfoDto(roomId, roomInfo);

        return roomId;
    }

//    public AlgoQuestionResponse[] getAlgoQuestion(String roomId, Difficulty difficulty){
//
//
//
//        AlgoQuestionResponse[] algoQuestion = new AlgoQuestionResponse[2];
//
//
//        // 일단은 하드코딩
//        // ************* 이부분 사용자가 푼 문제 히스토리 참고해서 두 문제 조회하는 로직으로 변경. ****************
//        algoQuestion[0]=new AlgoQuestionResponse(0L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/SheepAndWolves.txt",
//                "양늑이");
//        algoQuestion[1]=new AlgoQuestionResponse(1L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/test.txt"
//        ,"늑양이");
//
//        roomInfoDto roomInfoDto=cachedatas.cacheroomInfoDto(roomId);
//
//        // ******************* 가져온 문제 아이디 캐싱 필요. ****************************
//        roomInfoDto.setProblem1Id(0L);
//        roomInfoDto.setProblem2Id(1L);
//        cachedatas.cacheroomInfoDto(roomId,roomInfoDto);
//
//
//        return algoQuestion;
//    }

    public List<ProblemTestcaseDto> getAlgoQuestion(String roomId, Level level){

        RoomInfoDto roomInfoDto = cachedatas.cacheroomInfoDto(roomId);

        List<ProblemTestcaseDto> selectedProblems = problemService.getTwoProblem(
            roomInfoDto.getManagerId(),
            roomInfoDto.getParticipantId(), level);

        roomInfoDto.setProblem1Id(selectedProblems.get(0).getProblemId());
        roomInfoDto.setProblem2Id(selectedProblems.get(1).getProblemId());

        cachedatas.cacheroomInfoDto(roomId,roomInfoDto);

        return selectedProblems;
    }

    public boolean setReadyInRoomInfo(readyInfoRequest readyInfoRequest) {

        String roomId = readyInfoRequest.getRoomId();
        RoomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
        boolean isHost = readyInfoRequest.isHost();

        // 준비 상태 업데이트
        updateReadyStatus(roomId, roomInfo, isHost);

        cachedatas.updateCodeCache(roomId, readyInfoRequest.getAlgoQuestionId().toString(), null);

        // 두 사용자가 모두 준비되었는지 확인
        if (areBothParticipantsReady(roomId, roomInfo)) {
            // 두 사용자가 모두 준비완료를 했을 경우.
            roomInfo.setRoomStatus(RoomStatus.IN_GAME);
//            roomInfo.setStartTime(LocalDateTime.now());
            return true;
        }


        return false;
    }

    public void updateReadyStatus(String roomId, RoomInfoDto roomInfo, boolean isHost) {
        lock.lock();
        try{
            if (isHost) {
                roomInfo.setManagerIsReady(true);

            } else {
                roomInfo.setParticipantIsReady(true);
            }
            cachedatas.cacheroomInfoDto(roomId, roomInfo);
        }finally {
            lock.unlock();
        }

    }

    public boolean areBothParticipantsReady(String roomId, RoomInfoDto roomInfo) {
        lock.lock();
        try {
            if (roomInfo.isManagerIsReady() && roomInfo.isParticipantIsReady()) {
                roomInfo.setManagerIsReady(false);
                roomInfo.setParticipantIsReady(false);
                cachedatas.cacheroomInfoDto(roomId, roomInfo);
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    public switchCodeResponse getParticipantsCode(switchCodeRequest switchCodeRequest){

        if(switchCodeRequest.getAlgoQuestionId().equals(cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem1Id())){
            List<UserCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem2Id().toString());
            return new switchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }else{
            List<UserCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem1Id().toString());
            return new switchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }
    }

    @Transactional
    public SubmitResponse submitCode(SubmitRequest submitRequest){
        // 제출 광클 방지 Lock -> AOP로 디벨롭하면 좋을듯
        String key = requestLockService.generateLockKey(GRADING, submitRequest.getRoomId(), submitRequest.isHost());
        requestLockService.ifLockedThrowExceptionElseLock(key, GRADING.getDuration());

        // db 조회 후 채점 서버로 Code 및 문제 데이터, 테케 전송
        // 결과 수신
        GradingResponse result = gradingService.getResult(submitRequest.getAlgoQuestionId(),
            submitRequest.getCode(), submitRequest.getLanguage());

        // 오답 일 경우 실패 응답 + 관련 메시지 전송
        // 정답 일 경우 DB에 정답 코드 저장.
        // 덕주 : 히스토리를 찾을 방법이 없는뎅.. history id를 저장하거나 history entity에 roomId를 저장해야할 듯합니다. 근데 roomId가 unique하다는 보장이 있는지 모르겠어어 이부분은 스킵할께요
//        if (result.isSucceeded()){
//            historyRepository.findById();
//        }


        // 페어프로그래밍 모드 전환 메시지 전송
        /*
        ex)
        submitResponse.setResult(false);
        submitResponse.setMessage("테케 1 정답\n 테케 2 오답");
        dto 채점서버 반환 형태에 따라 수정 필요하면 수정 후 API 명세에 적어주세용 -> 반환 엔티티 및 API 명세 수정했습니돠
         */

        // Lock 헤제
        requestLockService.unlock(key);

        return SubmitResponse.builder()
            .result(result.isSucceeded())
            .testcaseResults(result.getTestcaseResults())
            .build();
    }


}
