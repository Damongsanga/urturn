package com.ssafy.urturn.solving.dto.response;

import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;

import java.util.List;

public record ReadContextResponse (
        Long managerId,
        Long pairId,
        List<ProblemTestcaseDto> problemTestcases
){
}
