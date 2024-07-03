package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.request.GradeCreate;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final TokenCreator tokenCreator;
    private final ExecutionService executionService;

    @Transactional
    public List<Grade> createGrades(List<GradeCreate> gradeCreates) {
        // Grade 기본 생성 with token 생성
        List<Grade> grades = gradeCreates.stream()
                .map(Grade::from)
                .map(a -> a.setToken(tokenCreator.createToken()))
                .toList();

        // 저장

        // 채점 로직 실행
        grades.forEach(executionService::execute);

        return grades;
    }
}
