package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class readyInfoRequest {
    private String roomId;
    private Long algoQuestionId;

    @JsonProperty("isHost")
    private boolean isHost;
}
