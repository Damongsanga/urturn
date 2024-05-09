package com.ssafy.urturn.problem.service;

import static com.ssafy.urturn.global.exception.errorcode.CommonErrorCode.INTERNAL_SERVER_ERROR;
import static com.ssafy.urturn.problem.Grading.*;
import static com.ssafy.urturn.problem.SubmissionStatus.*;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.problem.Language;
import com.ssafy.urturn.problem.SubmissionStatus;
import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.dto.GradingTestcaseDto;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.SubmissionBatchResponseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import com.ssafy.urturn.problem.dto.TokenDto;
import com.ssafy.urturn.problem.dto.TokenBatchCreateDto;
import com.ssafy.urturn.problem.dto.SubmissionDto;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import java.util.List;
import java.util.Objects;
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

    // status Id -> status 변환 배열. 채점 서버가 변환되지 않을 예정이며 매번 확인할 필요가 없음
    private static final String[] statusIdToStatus = new String[]{
        "", "In Queue", "Processing", "Accepted", "Wrong Answer", "Time Limit Exceeded", "Compilation Error", "Runtime Error (SIGSEGV)", "Runtime Error (SIGXFSZ)", "Runtime Error (SIGFPE)", "Runtime Error (SIGABRT)", "Runtime Error (NZEC)", "Runtime Error (Other)", "Internal Error", "Exec Format Error"
    };

    // 정답인 status Id
    private final int answerStatusId = 3;

    public GradingResponse getResult(Long problemId, String inputCode, Language language){
        int count = 0;

        // 문제 id를 기반으로 문제 + 전체 테스트케이스 조회
        ProblemTestcaseDto problemTestcaseDto = problemRepository.getProblemAndTestcase(problemId).orElseThrow(() -> new RestApiException(
            CustomErrorCode.NO_PROBLEM));
        log.info("problemId : {}", problemId);
        for (TestcaseDto testcase : problemTestcaseDto.getTestcases()) {
            log.info("testcase : {}", testcase.getId());
        }

        log.info("language : {}", language.toString());
        log.info("language id: {}", language.getLangId());

        // 제출한 코드를 기반으로 채점
        // 토큰 추출
        List<TokenDto> tokens = createTokens(problemTestcaseDto.getTestcases(), inputCode, language).block();

        for (TokenDto token : tokens) {
            log.info("token : {}", token);
        }

        SubmissionBatchResponseDto submissionsBatchResponse = null;
        SubmissionStatus status = PROCESSING;

        // 3초 대기 & 총 5회 재요청.
        outer : while(count < 5){
            // 3초 대기, 바로 토큰 결과를 요청하면 채점 서버에서 완료하지 못함
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){
                throw new RestApiException(INTERNAL_SERVER_ERROR);
            }
            // 응답받은 토큰을 기반으로 다시 응답 추출
            submissionsBatchResponse = getSubmissions(tokens).block();

            switch (status = confirmStatus(Objects.requireNonNull(submissionsBatchResponse).getSubmissions())){
                case PROCESSING -> {
                    count++;
                }
                case ALL_ACCEPTED, FAILED -> {
                    break outer;
                }
            }
        }

        // 재요청 후에도 계속 채점 서버에서 대기중이면 나중에 요청하도록 에러 메세지 반환
        if (status.equals(PROCESSING))
            throw new RestApiException(CustomErrorCode.TOO_MUCH_TRAFFIC);

        // 채점 성공 여부 & 테스트케이스별 응답 반환
        return makeResponse(submissionsBatchResponse.getSubmissions(), problemId, inputCode, status);
    }

    // 응답을 확인하면서 전부 Accepted일 때만 결과 반환
    private GradingResponse makeResponse(List<SubmissionDto> submissions, Long problemId, String inputCode, SubmissionStatus status) {
        List<GradingTestcaseDto> testcaseResults =
            submissions.stream().map(sub -> new GradingTestcaseDto(statusIdToStatus[sub.getStatusId()], sub.getStderr())).toList();

        return GradingResponse.builder()
            .problemId(problemId)
            .inputCode(inputCode)
            .succeeded(status.isSuccess())
            .testcaseResults(testcaseResults)
            .build();
    }

    /**
     * submissions에 대한 상태
     * ALL_ACCEPTED : 모든 submission이 Accepted (ID = 3)
     * FAILED : 한 개의 submission이라도 Failed (ID > 3)
     * PROCESSING : 모든 submission에 대해 FAIL이 발생하지 않았으나 완료되지 않은 submission이 있음 (ID < 3)
      */
    private SubmissionStatus confirmStatus(List<SubmissionDto> submissions){
        boolean processingFlag = false;
        for (SubmissionDto sub : submissions) {
            if (sub.getStatusId() > answerStatusId) return FAILED;
            if (sub.getStatusId() < answerStatusId) processingFlag = true;
        }
        if (processingFlag) return PROCESSING;
        return ALL_ACCEPTED;
    }

    // 채점 서버에 inputCode들을 제공하여 토큰을 반환받음
    private Mono<List<TokenDto>> createTokens(List<TestcaseDto> testcases, String inputCode, Language language){

        return webClient.post().uri(uriBuilder -> uriBuilder
            .path(CREATE_TOKENS.getPath())
            .query(CREATE_TOKENS.getQuery())
            .build())
            .body(BodyInserters
                .fromValue(new TokenBatchCreateDto(testcases, inputCode, language)))
            .retrieve()
            .bodyToFlux(TokenDto.class)
            .collectList();
    }

    // 채점 서버에 token들을 제공하여 submission(결과)를 제공받음
    private Mono<SubmissionBatchResponseDto> getSubmissions(List<TokenDto> tokens){
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(GET_GRADES.getPath())
                .query(GET_GRADES.getQuery(tokens))
                .build()).retrieve()
            .bodyToMono(SubmissionBatchResponseDto.class);
    }

}
