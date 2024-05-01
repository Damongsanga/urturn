package com.ssafy.urturn.problem.service;

import static com.ssafy.urturn.problem.Grading.*;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.dto.GradingTestcaseDto;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.SubmissionBatchResponseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import com.ssafy.urturn.problem.dto.TokenDto;
import com.ssafy.urturn.problem.dto.TokenBatchCreateDto;
import com.ssafy.urturn.problem.dto.SubmissionDto;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradingService {

    private final WebClient webClient;
    private final ProblemRepository problemRepository;
    private final String[] status = new String[]{
        "", "In Queue", "Processing", "Accepted", "Wrong Answer", "Time Limit Exceeded", "Compilation Error", "Runtime Error (SIGSEGV)", "Runtime Error (SIGXFSZ)", "Runtime Error (SIGFPE)", "Runtime Error (SIGABRT)", "Runtime Error (NZEC)", "Runtime Error (Other)", "Internal Error", "Exec Format Error"
    };

    private final int answerStatusId = 3;

    public GradingResponse getResult(Long problemId, String inputCode) throws InterruptedException {
        // 문제 id를 기반으로 문제 + 전체 테스트케이스 조회
        ProblemTestcaseDto problemTestcaseDto = problemRepository.getProblemAndTestcase(problemId).orElseThrow(() -> new RestApiException(
            CustomErrorCode.NO_PROBLEM));

        // 제출한 코드를 기반으로 채점
        // 토큰 추출
        List<TokenDto> tokens = createTokens(problemTestcaseDto.getTestcases(), inputCode).block();

        for (TokenDto token : tokens) {
            log.info("token : {}", token);
        }

        Thread.sleep(10000);

        // 응답받은 토큰을 기반으로 다시 응답 추출
        // 여기서 문제 발생, 바로 보내니까 In Queue 반환함
        SubmissionBatchResponseDto submissionsBatchResponse = getSubmissions(tokens).block();

        // 채점 성공 여부 & 테스트케이스별 응답 반환
        assert submissionsBatchResponse != null;
        return makeResponse(submissionsBatchResponse.getSubmissions(), problemId, inputCode);
    }

    private GradingResponse makeResponse(List<SubmissionDto> submissions, Long problemId, String inputCode) {
        boolean succeeded = true;
        List<GradingTestcaseDto> testcaseResults = new ArrayList<>();
        for (SubmissionDto sub : submissions) {
            if (sub.getStatusId() != answerStatusId) succeeded = false;
            testcaseResults.add(new GradingTestcaseDto(status[sub.getStatusId()], sub.getStderr()));
        }
        return GradingResponse.builder()
            .problemId(problemId)
            .inputCode(inputCode)
            .succeeded(succeeded)
            .testcaseResults(testcaseResults)
            .build();
    }

    // 채점 따로 메서드
    private Mono<List<TokenDto>> createTokens(List<TestcaseDto> testcases, String inputCode){

        return webClient.post().uri(uriBuilder -> uriBuilder
            .path(CREATE_TOKENS.getPath())
            .query(CREATE_TOKENS.getQuery())
            .build())
            .body(BodyInserters
                .fromValue(new TokenBatchCreateDto(testcases, inputCode)))
            .retrieve()
            .bodyToFlux(TokenDto.class)
            .collectList();
    }

    private Mono<SubmissionBatchResponseDto> getSubmissions(List<TokenDto> tokens){
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(GET_GRADES.getPath())
                .query(GET_GRADES.getQuery(tokens))
                .build()).retrieve()
            .bodyToMono(SubmissionBatchResponseDto.class);
    }


}
