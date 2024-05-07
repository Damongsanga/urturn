package com.ssafy.urturn.problem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GradingResponse {

    private Long problemId;
    private String inputCode;
    private boolean succeeded;
    private List<GradingTestcaseDto> testcaseResults;

    @Builder
    public GradingResponse(Long problemId, String inputCode, boolean succeeded,
        List<GradingTestcaseDto> testcaseResults) {
        this.problemId = problemId;
        this.inputCode = inputCode;
        this.succeeded = succeeded;
        this.testcaseResults = testcaseResults;
    }
}
