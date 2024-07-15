package com.ssafy.urturn.problem.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.*;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.dto.request.ProblemCreateRequest;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.entity.MemberProblem;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.repository.MemberProblemRepository;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;

    private final MemberProblemRepository memberProblemRepository;
    private final MemberRepository memberRepository;

    // ADMIN만 접근할 수 있도록 검증해야함
    @Transactional
    public Long createProblem(ProblemCreateRequest req) {

        Problem problem = Problem.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .level(req.getLevel())
                .build();

        return problemRepository.save(problem).getId();
    }


    // 리턴값으로 Response 객체로 감싸주는게 컨벤션상 좋지만.. 완전 동일한 객체를 굳이 2개 만들어야 하는지 의문
    public ProblemTestcaseDto getProblemWithPublicTestcase(Long problemId) {
        return problemRepository.getProblemWithPublicTestcase(problemId).orElseThrow(() -> new RestApiException(
            NO_PROBLEM));
    }

    public ProblemTestcaseDto getProblem(Long problemId) {
        return problemRepository.getProblemAndTestcase(problemId).orElseThrow(() -> new RestApiException(
            NO_PROBLEM));
    }

    // 문제 2개랑 공개테스트케이스 반환
    public List<ProblemTestcaseDto> getTwoProblems(Long managerId, Long pairId, Level level){
        return problemRepository.selectTwoProblemsByLevel(level, managerId, pairId);
    }

    @Transactional
    public void saveMemberProblem(Long managerId, Long pairId, Long problemId){
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new RestApiException(NO_PROBLEM));
        Member manager = memberRepository.findById(managerId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        Member pair = memberRepository.findById(pairId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        memberProblemRepository.save(MemberProblem.builder()
            .problem(problem)
            .member(manager).build());
        memberProblemRepository.save(MemberProblem.builder()
            .problem(problem)
            .member(pair).build());
    }

}
