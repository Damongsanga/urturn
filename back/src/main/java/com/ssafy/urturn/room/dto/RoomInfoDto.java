package com.ssafy.urturn.room.dto;

import com.ssafy.urturn.room.RoomStatus;
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

    private Long historyId;

    private boolean managerIsSubmitting;
    private boolean pairIsSubmitting;

}
