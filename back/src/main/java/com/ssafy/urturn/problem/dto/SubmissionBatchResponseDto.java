package com.ssafy.urturn.problem.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubmissionBatchResponseDto {
    private List<SubmissionDto> submissions;
}
