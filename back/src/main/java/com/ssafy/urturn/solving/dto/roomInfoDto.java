package com.ssafy.urturn.solving.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class roomInfoDto {
    private Long managerId;
    private Long participantId;
    private RoomStatus roomStatus;
    private boolean managerIsReady;
    private boolean participantIsReady;


}
