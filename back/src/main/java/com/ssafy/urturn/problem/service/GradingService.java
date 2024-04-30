package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import com.ssafy.urturn.problem.repository.TestcaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final ProblemRepository problemRepository;
    private final TestcaseRepository testcaseRepository;

    public GradingResponse getResult(Long problemId, String inputCode){
        // 문제 id를 기반으로 문제 + 전체 테스트케이스 조회

        // 제출한 코드를 기반으로 채점

        // 전부 통과인지 확인

        // 채점 성공 여부 & 테스트케이스별 응답 반환

        return null;
    }

    // 채점 따로 메서드
    public void grade(){

    }

}
