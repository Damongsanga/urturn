package com.ssafy.urturn.solving.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class roomInfoResponse {
    private String roomId;
    private String entryCode;
    private boolean isHost;
}
