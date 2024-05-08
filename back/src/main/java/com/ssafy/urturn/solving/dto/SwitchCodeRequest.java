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

    @Override
    public String toString() {
        return "switchCodeRequest{" +
                "code='" + code + '\'' +
                ", roomId='" + roomId + '\'' +
                ", problemId=" + problemId +
                ", round=" + round +
                ", isHost=" + isHost +
                '}';
    }
}
