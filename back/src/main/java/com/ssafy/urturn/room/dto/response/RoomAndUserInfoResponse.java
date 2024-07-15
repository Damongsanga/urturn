package com.ssafy.urturn.room.dto.response;

public record RoomAndUserInfoResponse(
        RoomInfoResponse roomInfoResponse,
        Long managerId,
        UserInfoResponse managerInfoResponse,
        Long pairId,
        UserInfoResponse pairInfoResponse
){
    public RoomAndUserInfoResponse(RoomInfoResponse roomInfoResponse, Long managerId, UserInfoResponse managerInfoResponse) {
        this(roomInfoResponse, managerId, managerInfoResponse, null, null);
    }
}
