package com.ssafy.urturn.solving.service;

import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.solving.cache.cacheDatas;
import com.ssafy.urturn.solving.dto.*;
import com.ssafy.urturn.solving.temp.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.ssafy.urturn.solving.dto.RoomStatus.IN_GAME;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final MemberService memberService;
    private final cacheDatas cachedatas;
    private final WebSocketSessionManager webSocketSessionManager;
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

    public algoQuestionResponse[] getAlgoQuestion(Difficulty difficulty){
        // DB에서 문제 추출
        algoQuestionResponse[] algoQuestion = new algoQuestionResponse[2];


        // 일단은 하드코딩
        algoQuestion[0]=new algoQuestionResponse(0L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/SheepAndWolves.txt",
                "양늑이");
        algoQuestion[1]=new algoQuestionResponse(1L,"https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/AlgoQuestion/test.txt"
        ,"늑양이");

        return algoQuestion;
    }

    public boolean setReadyInRoomInfo(readyInfoRequest readyInfoRequest) {
        String roomId = readyInfoRequest.getRoomId();
        roomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
        boolean isHost = readyInfoRequest.isHost();

        // 준비 상태 업데이트
        updateReadyStatus(roomInfo, isHost);
        cachedatas.updateCodeCache(roomId, readyInfoRequest.getAlgoQuestionId().toString(), null);

        // 두 사용자가 모두 준비되었는지 확인
        if (areBothParticipantsReady(roomInfo)) {
            roomInfo.setRoomStatus(RoomStatus.IN_GAME);
            return true;
        }

        return false;
    }

    private void updateReadyStatus(roomInfoDto roomInfo, boolean isHost) {
        if (isHost) {
            roomInfo.setManagerIsReady(true);
        } else {
            roomInfo.setParticipantIsReady(true);
        }
    }

    private boolean areBothParticipantsReady(roomInfoDto roomInfo) {
        return roomInfo.isManagerIsReady() && roomInfo.isParticipantIsReady();
    }




}
