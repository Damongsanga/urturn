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
    private Long pairId;
    private RoomStatus roomStatus;
    private boolean managerIsReady;
    private boolean pairIsReady;
    // 문제1 ID
    private Long problem1Id;
    // 문제2 ID
    private Long problem2Id;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
    private Long historyId; // 추가해야함

}
