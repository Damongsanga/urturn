package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.dto.ProblemCreateRequest;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
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
    public Problem createProblem(ProblemCreateRequest req) {

        Problem problem = Problem.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .level(req.getLevel())
                .build();

        return problemRepository.save(problem);
    }

    // dto 변환 필요. TC까지 필요한지 여부에 따라 응답 다름
    public List<Problem> selectProblem(Level level, Long pairId){
        Long memberId = MemberUtil.getMemberId();
        return problemRepository.selectTwoProblemByLevel(level, memberId, pairId);
    }

    // DTO 변환 필요
    public Problem getProblem(Long problemId) {
        return problemRepository.findById(problemId).orElseThrow(() -> new RestApiException(CustomErrorCode.NO_PROBLEM));
    }
}
