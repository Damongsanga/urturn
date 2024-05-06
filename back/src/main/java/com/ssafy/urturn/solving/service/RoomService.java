package com.ssafy.urturn.solving.service;

import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.solving.cache.cacheDatas;
import com.ssafy.urturn.solving.dto.*;
import com.ssafy.urturn.solving.temp.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static com.ssafy.urturn.solving.dto.RoomStatus.IN_GAME;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final MemberService memberService;
    private final cacheDatas cachedatas;
    private final WebSocketSessionManager webSocketSessionManager;
    private final ReentrantLock lock;
    /*
    방생성
     */
    public roomInfoResponse createRoom(Long userId){
        // 방 ID
        String roomId= UUID.randomUUID().toString();
        // 입장코드
        String entryCode=UUID.randomUUID().toString().substring(0,6);

        // 방 정보 DTO 생성
        roomInfoDto roominfodto=new roomInfoDto();
        roominfodto.setManagerId(userId);
        roominfodto.setRoomStatus(RoomStatus.WAITING);

        // 초대코드 키로 방 ID 캐시
        cachedatas.cacheRoomId(entryCode,roomId);

        // 방 ID 키로 방정보 캐시
        cachedatas.cacheroomInfoDto(roomId,roominfodto);

        return new roomInfoResponse(roomId, entryCode, true);

    }

    public userInfoResponse getUserInfo(Long myUserId,Long relativeUserId){

        return memberService.getMemberInfo(myUserId,relativeUserId);
    }

    public String canEnterRoom(String entryCode) {
        // 캐시된 방 ID 가져오기
        String roomId = cachedatas.cacheRoomId(entryCode);
        if (roomId == null) {
            throw new RuntimeException("해당 방이 존재하지 않습니다.");
        }

        // 방 정보 가져오기
        roomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
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

    public algoQuestionResponse[] getAlgoQuestion(String roomId, Difficulty difficulty){



        algoQuestionResponse[] algoQuestion = new algoQuestionResponse[2];


        // 일단은 하드코딩
        // ************* 이부분 사용자가 푼 문제 히스토리 참고해서 두 문제 조회하는 로직으로 변경. ****************
        algoQuestion[0]=new algoQuestionResponse(0L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/SheepAndWolves.txt",
                "양늑이");
        algoQuestion[1]=new algoQuestionResponse(1L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/test.txt"
        ,"늑양이");

        roomInfoDto roomInfoDto=cachedatas.cacheroomInfoDto(roomId);

        // ******************* 가져온 문제 아이디 캐싱 필요. ****************************
        roomInfoDto.setProblem1Id(0L);
        roomInfoDto.setProblem2Id(1L);
        cachedatas.cacheroomInfoDto(roomId,roomInfoDto);


        return algoQuestion;
    }

    public boolean setReadyInRoomInfo(readyInfoRequest readyInfoRequest) {

        String roomId = readyInfoRequest.getRoomId();
        roomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
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

    public void updateReadyStatus(String roomId, roomInfoDto roomInfo, boolean isHost) {
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

    public boolean areBothParticipantsReady(String roomId, roomInfoDto roomInfo) {
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
            List<userCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem2Id().toString());
            return new switchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }else{
            List<userCodeDto> codes= cachedatas.cacheCodes(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getProblem1Id().toString());
            return new switchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1);
        }
    }

    public submitResponse submitCode(submitRequest submitRequest){
        submitResponse submitResponse=new submitResponse();

        // db 조회 후 채점 서버로 Code 및 문제 데이터, 테케 전송

        // 결과 수신.

        // 오답 일 경우 실패 응답 + 관련 메시지 전송

        // 정답 일 경우

        //  DB에 정답 코드 저장.

        // 페어프로그래밍 모드 전환 메시지 전송

        /*
        ex)
        submitResponse.setResult(false);
        submitResponse.setMessage("테케 1 정답\n 테케 2 오답");
        dto 채점서버 반환 형태에 따라 수정 필요하면 수정 후 API 명세에 적어주세용
         */


        return submitResponse;
    }


}
