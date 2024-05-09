package com.ssafy.urturn.solving.service;

import static com.ssafy.urturn.global.RequestLockType.*;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.*;

import com.ssafy.urturn.global.RequestLockService;
import com.ssafy.urturn.global.RequestLockType;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.entity.History;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService {
    private final MemberService memberService;
    private final GradingService gradingService;
    private final ProblemService problemService;
    private final cacheDatas cachedatas;
    private final ReentrantLock lock;
    private final HistoryRepository historyRepository;

    private final RequestLockService requestLockService;


    /**
    방생성
     */
    @Transactional
    public RoomInfoResponse createRoom(Long userId){
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

        return new RoomInfoResponse(roomId, entryCode, true);

    }

    public UserInfoResponse getUserInfo(Long myUserId,Long pairId){
        return memberService.getMemberInfo(myUserId,pairId);
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
        roomInfo.setPairId(MemberUtil.getMemberId());

        // 방 정보 업데이트
        cachedatas.cacheroomInfoDto(roomId, roomInfo);

        return roomId;
    }

//    public ProblemResponse[] getproblem(String roomId, Difficulty difficulty){
//
//
//
//        ProblemResponse[] problem = new ProblemResponse[2];
//
//
//        // 일단은 하드코딩
//        // ************* 이부분 사용자가 푼 문제 히스토리 참고해서 두 문제 조회하는 로직으로 변경. ****************
//        problem[0]=new ProblemResponse(0L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/problem/SheepAndWolves.txt",
//                "양늑이");
//        problem[1]=new ProblemResponse(1L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/problem/test.txt"
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
//        return problem;
//    }

    public List<ProblemTestcaseDto> getproblem(String roomId, Level level){

        RoomInfoDto roomInfoDto = cachedatas.cacheroomInfoDto(roomId);

        List<ProblemTestcaseDto> selectedProblems = problemService.getTwoProblem(
            roomInfoDto.getManagerId(),
            roomInfoDto.getPairId(), level);

        roomInfoDto.setProblem1Id(selectedProblems.get(0).getProblemId());
        roomInfoDto.setProblem2Id(selectedProblems.get(1).getProblemId());

        cachedatas.cacheroomInfoDto(roomId,roomInfoDto);

        return selectedProblems;
    }

    public boolean setReadyInRoomInfo(ReadyInfoRequest readyInfoRequest) {

        String roomId = readyInfoRequest.getRoomId();
        RoomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
        boolean isHost = readyInfoRequest.isHost();

        // 준비 상태 업데이트
        updateReadyStatus(roomId, roomInfo, isHost);

        cachedatas.updateCodeCache(roomId, readyInfoRequest.getProblemId().toString(), null);

        // 두 사용자가 모두 준비되었는지 확인
        if (areBothpairsReady(roomId, roomInfo)) {
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
                roomInfo.setPairIsReady(true);
            }
            cachedatas.cacheroomInfoDto(roomId, roomInfo);
        }finally {
            lock.unlock();
        }

    }

    public boolean areBothpairsReady(String roomId, RoomInfoDto roomInfo) {
        lock.lock();
        try {
            if (roomInfo.isManagerIsReady() && roomInfo.isPairIsReady()) {
                roomInfo.setManagerIsReady(false);
                roomInfo.setPairIsReady(false);
                cachedatas.cacheroomInfoDto(roomId, roomInfo);
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    public SwitchCodeResponse getPairsCode(SwitchCodeRequest switchCodeRequest){

        if(switchCodeRequest.getProblemId().equals(cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem1Id())){
            List<UserCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem2Id().toString());
            return new SwitchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }else{
            List<UserCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem1Id().toString());
            return new SwitchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }
    }

    // GradeService로 이동하면 좋을 것 같습니다.
    @Transactional
    public SubmitResponse submitCode(SubmitRequest submitRequest){

        // 제출 광클 방지 Lock by Redis-> AOP로 디벨롭하면 좋을듯
//        String key = requestLockService.generateLockKey(GRADING, submitRequest.getRoomId(), submitRequest.isHost());
//        requestLockService.ifLockedThrowExceptionElseLock(key, GRADING.getDuration());

        // db 조회 후 채점 서버로 Code 및 문제 데이터, 테케 전송
        // 결과 수신
        GradingResponse gradingResponse = gradingService.getResult(submitRequest.getProblemId(),
            submitRequest.getCode(), submitRequest.getLanguage());

        // 오답 일 경우 실패 응답 + 관련 메시지 전송
        // 정답 일 경우 DB에 정답 코드 저장.

        if (gradingResponse.isSucceeded()){
            Long historyId = cachedatas.cacheroomInfoDto(submitRequest.getRoomId()).getHistoryId();

            historyRepository.findById(historyId).orElseThrow(() -> new RestApiException(NO_HISTORY))
                .setCode(submitRequest.getProblemId(), submitRequest.getCode());
        }

        // 페어프로그래밍 모드 전환 메시지 전송
        /*
        ex)
        submitResponse.setResult(false);
        submitResponse.setMessage("테케 1 정답\n 테케 2 오답");
        dto 채점서버 반환 형태에 따라 수정 필요하면 수정 후 API 명세에 적어주세용 -> 반환 엔티티 및 API 명세 수정했습니돠
         */

        // Lock 해제
//        requestLockService.unlock(key);

        // 결과 반환
        return SubmitResponse.builder()
            .result(gradingResponse.isSucceeded())
            .testcaseResults(gradingResponse.getTestcaseResults())
            .build();
    }


    @Transactional
    public Map<Long, RetroCodeResponse> makeRetroCodeResponse(String roomId){

        Map<Long, RetroCodeResponse> map= new HashMap<>();
        RoomInfoDto roomInfoDto = cachedatas.cacheroomInfoDto(roomId);
        History history = historyRepository.findById(roomInfoDto.getHistoryId())
                .orElseThrow(()->new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND, "히스토리를 찾을 수 없습니다"));

        log.info("code 1 : {}", history.getCode1());
        log.info("code 2 : {}", history.getCode2());

        RetroCodeResponse problem1CodeResponse=new RetroCodeResponse(cachedatas.cacheCodes(roomId, roomInfoDto.getProblem1Id().toString())
                , history.getCode1());
        RetroCodeResponse problem2CodeResponse=new RetroCodeResponse(cachedatas.cacheCodes(roomId, roomInfoDto.getProblem2Id().toString())
                , history.getCode2());
        map.put(roomInfoDto.getProblem1Id(), problem1CodeResponse);
        map.put(roomInfoDto.getProblem2Id(), problem2CodeResponse);

        return map;
    }
    @Transactional
    public void updateHistory(String roomId, HistoryResult result, int totalRound){
        History history = historyRepository.findById(cachedatas.cacheroomInfoDto(roomId).getHistoryId())
                .orElseThrow(() -> new RestApiException(NO_HISTORY));
        history.finalizeUpdateHistory(result, totalRound);
        historyRepository.save(history);
    }
    @Transactional
    public void updateRetro(RetroCreateRequest req, Long historyId) {
        historyRepository.findById(historyId).orElseThrow(() -> new RestApiException(NO_HISTORY))
                .setRetro(req);
    }
}
