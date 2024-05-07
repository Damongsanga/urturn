package com.ssafy.urturn.solving.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlgoQuestionResponse {
    private Long algoQuestionId;
    private String algoQuestionUrl;
    private String algoQuestionTitle;
}
