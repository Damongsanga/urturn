package com.ssafy.urturn.solving.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomInfoDto {
    // 방장 ID
    private Long managerId;
    // 팀원 ID
    private Long participantId;
    private RoomStatus roomStatus;
    private boolean managerIsReady;
    private boolean participantIsReady;
    // 문제1 ID
    private Long problem1Id;
    // 문제2 ID
    private Long problem2Id;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;

}
