package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.problem.Language;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class SubmitRequest {
    private static final Logger log = LoggerFactory.getLogger(SubmitRequest.class);
    private String code;

    private String roomId;
    private Language language;
    @JsonProperty("algoQuestionId")
    private Long problemId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;
    @JsonProperty("isPair")
    private boolean isPair;

    @Override
    public String toString() {
        return "SubmitRequest{" +
                "code='" + code + '\'' +
                ", roomId='" + roomId + '\'' +
                ", language=" + language +
                ", problemId=" + problemId +
                ", round=" + round +
                ", isHost=" + isHost +
                ", isPair=" + isPair +
                '}';
    }
}
