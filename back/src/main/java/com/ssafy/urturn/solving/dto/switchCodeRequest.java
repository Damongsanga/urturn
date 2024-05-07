package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class switchCodeRequest {
    private String code;
    private String roomId;
    private Long algoQuestionId;
    private int round;
    @JsonProperty("isHost")
    private boolean isHost;

    @Override
    public String toString() {
        return "switchCodeRequest{" +
                "code='" + code + '\'' +
                ", roomId='" + roomId + '\'' +
                ", algoQuestionId=" + algoQuestionId +
                ", round=" + round +
                ", isHost=" + isHost +
                '}';
    }
}
