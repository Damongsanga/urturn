package com.ssafy.urturn.room.dto.response;

import com.ssafy.urturn.room.dto.LeaveRoomDto;

public record LeaveRoomResponse (
        Long managerId,
        Long pairId,
        LeaveRoomDto leaveRoomDto
){
}
