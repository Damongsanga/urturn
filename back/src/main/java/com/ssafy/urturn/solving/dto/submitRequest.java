package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class submitRequest {
    private String code;
    private String roomId;
    private Long algoQuestionId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;
}
