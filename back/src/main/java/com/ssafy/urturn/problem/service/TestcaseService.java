package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.problem.dto.TestcaseCreateRequest;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.entity.Testcase;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import com.ssafy.urturn.problem.repository.TestcaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TestcaseService {

    private final TestcaseRepository testcaseRepository;
    private final ProblemRepository problemRepository;

    // ADMIN만 접근할 수 있도록 검증해야함
    @Transactional
    public List<Long> createTestcases(List<TestcaseCreateRequest> reqs, Long problemId) {

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new RestApiException(CustomErrorCode.NO_PROBLEM));
        List<Testcase> list = reqs.stream().map(req -> new Testcase(req, problem)).toList();

        List<Long> res = new ArrayList<>();
        for (Testcase testcase : list) {
            res.add(testcaseRepository.save(testcase).getId());
        }
        return res;
    }

}
