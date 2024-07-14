package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.problem.Language;
import com.ssafy.urturn.problem.dto.SubmissionBatchResponseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import com.ssafy.urturn.problem.dto.TokenBatchCreateDto;
import com.ssafy.urturn.problem.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ssafy.urturn.problem.Grading.CREATE_TOKENS;
import static com.ssafy.urturn.problem.Grading.GET_GRADES;

@Component
@RequiredArgsConstructor
public class GradingServerClient {

    private final WebClient webClient;
    // 채점 서버에 inputCode들을 제공하여 토큰을 반환받음
    public Mono<List<TokenDto>> createTokens(List<TestcaseDto> testcases, String inputCode, Language language){

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
    public Mono<SubmissionBatchResponseDto> getSubmissions(List<TokenDto> tokens){
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(GET_GRADES.getPath())
                        .query(GET_GRADES.getQuery(tokens))
                        .build()).retrieve()
                .bodyToMono(SubmissionBatchResponseDto.class) //BASE64 -> UTF-8로 인코딩
                .map(submissionBatch -> {
                    submissionBatch.decodeBase64toUTF8();
                    return submissionBatch;
                });
    }
}
