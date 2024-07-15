package com.ssafy.urturn.room.service;

import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.room.dto.response.RoomAndUserInfoResponse;
import com.ssafy.urturn.room.dto.request.RoomIdRequest;
import com.ssafy.urturn.room.dto.response.RoomInfoResponse;
import com.ssafy.urturn.room.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomWebsocketService {

    private final RoomService roomService;
    private final CacheDatas cacheDatas;

    @Transactional
    public RoomAndUserInfoResponse createRoom(Long managerId){
        // 해당 유저가 최근에 플레이한 방에 대한 캐시 데이터 삭제
        roomService.deleteRoomCaches(managerId.toString());

        RoomInfoResponse roomInfoResponse = roomService.createRoom(managerId);
        UserInfoResponse managerInfoResponse = roomService.getUserInfo(managerId, null);

        return new RoomAndUserInfoResponse(roomInfoResponse, managerId, managerInfoResponse);
    }

    @Transactional
    public RoomAndUserInfoResponse enterRoom(RoomIdRequest roomIddto){
        String roomId = roomIddto.getRoomId();
        Long pairId = roomService.getPairIdFromCache(roomId);
        Long managerId = roomService.getManagerIdFromCache(roomId);

        // 해당 유저가 최근에 플레이한 방에 대한 캐시 데이터 삭제
        roomService.deleteRoomCaches(pairId.toString());
        // 현재 유저의 최근 방 정보 수정
        cacheDatas.putRecentRoomId(pairId.toString(), roomId);

        // Pair 정보 반환
        UserInfoResponse managerInfoResponse = roomService.getUserInfo(pairId, managerId);
        RoomInfoResponse roomInfoResponse = RoomInfoResponse.makeResponseForPair(roomId);
        // Manager 정보 반환
        UserInfoResponse pairInfoResponse = roomService.getUserInfo(managerId, pairId);

        return new RoomAndUserInfoResponse(roomInfoResponse, managerId, managerInfoResponse, pairId, pairInfoResponse);
    }


}
