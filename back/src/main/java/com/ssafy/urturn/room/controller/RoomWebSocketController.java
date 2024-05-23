package com.ssafy.urturn.room.controller;

import com.ssafy.urturn.room.dto.*;
import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomService roomService;
    private final CacheDatas cacheDatas;

    @MessageMapping("/createRoom")
    public void createRoom(@Payload MemberIdDto memberIdDto) {
        log.info("방 생성 로직 멤버ID Dto = {}", memberIdDto);

        Long userId = memberIdDto.getMemberId();
        // 해당 유저가 최근에 플레이한 방에 대한 캐시 데이터 삭제
        roomService.deleteRoomCaches(userId.toString());

        RoomInfoResponse roomInfoResponse = roomService.createRoom(userId);
        UserInfoResponse userInfoResponse = roomService.getUserInfo(userId, null);
        sendUserAndRoomInfo(userId, roomInfoResponse, userInfoResponse);

        // response에 포함된 방 정보를 이용하여 방을 생성한 사용자에게만 응답을 보냄
        log.info("roomInfoResponse : {}", roomInfoResponse);
        log.info("userInfoResponse : {}", userInfoResponse);
    }

    @MessageMapping("/enterRoom")
    public void enterRoom(@Payload RoomIdDto roomIddto) {
        log.info("방 입장 RoomID Dto = {}", roomIddto);
        String roomId = roomIddto.getRoomId();
        Long pairId = getPairIdFromCache(roomId);
        Long managerId = getManagerIdFromCache(roomId);


        // 해당 유저가 최근에 플레이한 방에 대한 캐시 데이터 삭제
        roomService.deleteRoomCaches(pairId.toString());
        // 현재 유저의 최근 방 정보 수정
        cacheDatas.putRecentRoomId(pairId.toString(), roomId);

        // Pair 정보 반환
        UserInfoResponse managerInfoResponse = roomService.getUserInfo(pairId, managerId);
        RoomInfoResponse roomInfoResponse = RoomInfoResponse.makeResponseForPair(roomId);
        sendUserAndRoomInfo(pairId, roomInfoResponse, managerInfoResponse);

        // Manager 정보 반환
        UserInfoResponse pairInfoResponse = roomService.getUserInfo(managerId, pairId);
        sendPairInfoToManager(managerId, pairInfoResponse);

        log.info("roomInfoResponse : {}", roomInfoResponse);
        log.info("managerInfoResponse : {}", managerInfoResponse);
        log.info("pairInfoResponse : {}", pairInfoResponse);
    }



    @MessageMapping("/leaveRoom")
    public void leaveRoom(@Payload LeaveRoomDto leaveRoomDto) {
        Long pairId = getPairIdFromCache(leaveRoomDto.getRoomId());
        Long managerId = getManagerIdFromCache(leaveRoomDto.getRoomId());
        roomService.leaveRoom(leaveRoomDto);

        sendDisconnectionRequest(leaveRoomDto, pairId, managerId);
    }



    @MessageMapping("/sendOVSession")
    public void sendOVSSession(@Payload SessionIdDto sessionIdDto) {
        log.info("sessionId: {}", sessionIdDto.getSessionId());
        Long pairId = getPairIdFromCache(sessionIdDto.getRoomId());
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/receiveOVSession", sessionIdDto.getSessionId());
    }


    private void sendPairInfoToManager(Long managerId, UserInfoResponse pairInfoResponse) {
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/userInfo", pairInfoResponse);
    }

    private void sendUserAndRoomInfo(Long userId, RoomInfoResponse roomInfoResponse, UserInfoResponse userInfoResponse) {
        simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/roomInfo", roomInfoResponse);
        simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/userInfo", userInfoResponse);
    }

    private void sendDisconnectionRequest(LeaveRoomDto leaveRoomDto, Long pairId, Long managerId) {
        if (leaveRoomDto.isHost()) {
            simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/disconnectWebSocket", true);
            simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/disconnectWebSocket", true);
            return;
        }
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/disconnectWebSocket", true);
    }

    private Long getManagerIdFromCache(String roomId) {
        return cacheDatas.getRoomInfo(roomId).getManagerId();
    }

    private Long getPairIdFromCache(String roomId) {
        return cacheDatas.getRoomInfo(roomId).getPairId();
    }

}