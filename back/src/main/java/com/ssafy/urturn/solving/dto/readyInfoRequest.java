package com.ssafy.urturn.solving.dto;

import lombok.Getter;

@Getter
public class readyInfoRequest {
    private String roomId;
    private boolean isHost;
    private Long algoQuestionId;
}
