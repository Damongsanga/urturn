package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import java.util.Optional;

public interface ProblemCustomRepository {

    Optional<ProblemTestcaseDto> getProblemAndTestcase(Long problemId);
    Optional<ProblemTestcaseDto> getProblemWithPublicTestcase(Long problemId);

}
