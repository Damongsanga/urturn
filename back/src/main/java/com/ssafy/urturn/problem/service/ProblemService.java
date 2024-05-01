package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.problem.dto.ProblemCreateRequest;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;

    // ADMIN만 접근할 수 있도록 검증해야함
    // DTO 변환 필요
    @Transactional
    public Long createProblem(ProblemCreateRequest req) {

        Problem problem = Problem.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .level(req.getLevel())
                .build();

        return problemRepository.save(problem).getId();
    }

    // DTO 변환 필요
    public ProblemTestcaseDto getProblemWithPublicTestcase(Long problemId) {

        return problemRepository.getProblemWithPublicTestcase(problemId).orElseThrow(() -> new RestApiException(
            CustomErrorCode.NO_PROBLEM));

    }
}
