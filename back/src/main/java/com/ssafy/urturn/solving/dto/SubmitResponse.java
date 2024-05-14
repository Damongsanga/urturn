package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.problem.dto.GradingTestcaseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SubmitResponse {
    // 정답 유무
    private boolean result;

    private List<GradingTestcaseDto> testcaseResults;
}
