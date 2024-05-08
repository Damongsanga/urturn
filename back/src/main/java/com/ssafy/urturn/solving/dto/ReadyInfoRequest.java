package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReadyInfoRequest {
    private String roomId;
    @JsonProperty("algoQuestionId")
    private Long problemId;

    @JsonProperty("isHost")
    private boolean isHost;
}
