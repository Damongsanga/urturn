package com.ssafy.urturn.solving.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitRequest {
    private static final Logger log = LoggerFactory.getLogger(SubmitRequest.class);
    private String code;
    private String roomId;
    private Language language;
    @JsonProperty("algoQuestionId")
    private Long problemId;
    private int round;
    // 방장 여부
    @JsonProperty("isHost")
    private boolean isHost;
    // 페어프로그래밍 모드 여부
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
