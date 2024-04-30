package com.ssafy.urturn.problem.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class GradingResponse {

    private Long problemId;
    private boolean succeeded;
    private List<GradingTestcaseDto> testcaseResults;


}
