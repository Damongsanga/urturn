package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.problem.Language;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class SwitchCodeRequest {
    private static final Logger log= LoggerFactory.getLogger(SwitchCodeRequest.class);
    private String code;
    private String roomId;
    @JsonProperty("algoQuestionId")
    private Long problemId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;

    @JsonProperty("isPair")
    private boolean isPair;

    private Language language;

    @Override
    public String toString() {
        return "SwitchCodeRequest{" +
                "code='" + code + '\'' +
                ", roomId='" + roomId + '\'' +
                ", problemId=" + problemId +
                ", round=" + round +
                ", isHost=" + isHost +
                ", isPair=" + isPair +
                '}';
    }
}
