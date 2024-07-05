package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.request.GradeCreate;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import com.ssafy.urturn.grading.service.strategy.ExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {

    private final GradeRepository gradeRepository;
    private final TokenCreator tokenCreator;
    private final Map<String, ExecutionStrategy> strategyMap;

//    @Transactional
    public List<Grade> createGrades(List<GradeCreate> gradeCreates) {
        // Grade 기본 생성 with token 생성
        List<Grade> grades = gradeCreates.stream()
                .map(Grade::from)
                .map(a -> a.setToken(tokenCreator.createToken()))
                .toList();
        // 저장
        grades.forEach(gradeRepository::save);

        // 채점 로직 실행
        log.info("{}", strategyMap.keySet().toString());
        log.info("{}", strategyMap.values().toString());
        List<CompletableFuture<TokenWithStatus>> list = grades.stream().map(grade ->
                strategyMap.get(grade.getStrategyName()).execute(grade)).toList();

        list.forEach(r -> r.thenAccept(
                res -> log.info("Token : {}, STATUS : {}", res.getToken(), res.getStatus())
        ));

        return grades;
    }
}
