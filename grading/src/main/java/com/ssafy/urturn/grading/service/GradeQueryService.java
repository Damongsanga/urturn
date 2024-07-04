package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeQueryService {

    private final GradeRepository gradeRepository;

    public List<Grade> getGrades(List<String> tokens) {
        return tokens.stream().map(gradeRepository::findByToken).
                map(optional -> optional.orElse(Grade.empty())).toList();
    }
}