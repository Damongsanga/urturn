package com.ssafy.urturn.solving.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
public class ReadyInfoRequest {
    private static final Logger log = LoggerFactory.getLogger(ReadyInfoRequest.class);
    private String roomId;
    @JsonProperty("algoQuestionId")
    private Long problemId;

    @JsonProperty("isHost")
    private boolean isHost;

    @Override
    public String toString() {
        return "ReadyInfoRequest{" +
                "roomId='" + roomId + '\'' +
                ", problemId=" + problemId +
                ", isHost=" + isHost +
                '}';
    }
}
