package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.problem.Language;
import lombok.Getter;

@Getter
public class SubmitRequest {
    private String code;
    private String roomId;
    private Language language;
    private Long problemId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;
}
