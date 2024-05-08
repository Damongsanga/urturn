package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomInfoResponse {
    private String roomId;
    private String entryCode;
    private boolean isHost;
}
