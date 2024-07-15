package com.ssafy.urturn.room.controller;

import com.ssafy.urturn.room.dto.*;
import com.ssafy.urturn.room.dto.request.MemberIdRequest;
import com.ssafy.urturn.room.dto.request.RoomIdRequest;
import com.ssafy.urturn.room.dto.request.SessionIdRequest;
import com.ssafy.urturn.room.dto.response.LeaveRoomResponse;
import com.ssafy.urturn.room.dto.response.RoomAndUserInfoResponse;
import com.ssafy.urturn.room.service.RoomService;
import com.ssafy.urturn.room.service.RoomWebsocketService;
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
    private final RoomWebsocketService roomWebsocketService;

    // response에 포함된 방 정보를 이용하여 방을 생성한 사용자에게만 응답을 보냄
    @MessageMapping("/createRoom")
    public void createRoom(@Payload MemberIdRequest memberIdRequest) {
        log.info("방 생성 로직 멤버ID Dto = {}", memberIdRequest);
        sendUserAndRoomInfo(roomWebsocketService.createRoom(memberIdRequest.getMemberId()));
    }

    @MessageMapping("/enterRoom")
    public void enterRoom(@Payload RoomIdRequest roomIdRequest) {
        log.info("방 입장 RoomID Dto = {}", roomIdRequest);
        sendUserAndRoomInfo(roomWebsocketService.enterRoom(roomIdRequest));
    }

    @MessageMapping("/leaveRoom")
    public void leaveRoom(@Payload LeaveRoomDto leaveRoomDto) {
        LeaveRoomResponse response = roomService.leaveRoom(leaveRoomDto);
        sendDisconnectionRequest(response);
    }

    @MessageMapping("/sendOVSession")
    public void sendOVSSession(@Payload SessionIdRequest sessionIdRequest) {
        Long pairId = roomService.getPairIdFromCache(sessionIdRequest.getRoomId());
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/receiveOVSession", sessionIdRequest.getSessionId());
        log.info("sessionId: {}", sessionIdRequest.getSessionId());
    }

    private void sendUserAndRoomInfo(RoomAndUserInfoResponse response) {
        if (response.pairId() == null){
            simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/roomInfo", response.roomInfoResponse());
            simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/userInfo", response.managerInfoResponse());
            log.info("roomInfoResponse : {}", response.roomInfoResponse());
            log.info("userInfoResponse : {}", response.managerInfoResponse());
        } else {
            simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/roomInfo", response.roomInfoResponse());
            simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/userInfo", response.managerInfoResponse());
            simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/userInfo", response.pairInfoResponse());
            log.info("roomInfoResponse : {}", response.roomInfoResponse());
            log.info("managerInfoResponse : {}", response.managerInfoResponse());
            log.info("pairInfoResponse : {}", response.pairInfoResponse());
        }

    }

    private void sendDisconnectionRequest(LeaveRoomResponse response) {
        simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/disconnectWebSocket", true);
        if (response.leaveRoomDto().isHost()) {
            simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/disconnectWebSocket", true);
        }
    }


}