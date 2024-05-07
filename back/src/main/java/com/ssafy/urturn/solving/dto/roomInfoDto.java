package com.ssafy.urturn.solving.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class roomInfoDto {
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
