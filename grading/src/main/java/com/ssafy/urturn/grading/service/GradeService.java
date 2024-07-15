package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.dto.request.GradeCreateRequest;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.domain.strategy.ExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {

    private final GradeRepository gradeRepository;
    private final TokenManager tokenManager;
    private final Map<String, ExecutionStrategy> strategyMap;

    public List<Grade> createGrades(List<GradeCreateRequest> GradeCreateRequests) {
        // Grade 기본 생성 with token 생성
        List<Grade> grades = GradeCreateRequests.stream()
                .map(Grade::from)
                .map(a -> a.setToken(tokenManager.createToken()))
                .toList();
        // 저장
        grades.forEach(gradeRepository::save);

        // 채점 로직 실행
        log.info("{}", strategyMap.keySet().toString());
        log.info("{}", strategyMap.values().toString());

        grades.stream()
                .map(grade ->
                strategyMap.get(grade.getStrategyName()).execute(grade))
                .forEach(r -> r.thenAccept(
                res -> log.info("Token : {}, STATUS : {}", res.getToken(), res.getStatus())
        ));

        return grades;
    }
}
