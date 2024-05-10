package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SwitchCodeRequest {
    private String code;
    private String roomId;
    @JsonProperty("algoQuestionId")
    private Long problemId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;

    @JsonProperty("isPair")
    private boolean isPair;

}
