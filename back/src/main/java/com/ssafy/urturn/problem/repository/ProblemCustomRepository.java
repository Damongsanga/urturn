package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;

public interface ProblemCustomRepository {

    public ProblemTestcaseDto getProblemAndTestcase(Long problemId);

}
