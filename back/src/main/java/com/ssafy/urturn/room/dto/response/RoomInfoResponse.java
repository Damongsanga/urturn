package com.ssafy.urturn.room.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RoomInfoResponse {
    private String roomId;
    private String entryCode;
    private boolean isHost;

    public static RoomInfoResponse makeResponseForPair(String roomId){
        return new RoomInfoResponse(roomId, null, false);
    }
}
