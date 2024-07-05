package com.ssafy.urturn.grading.domain.repository;

import com.ssafy.urturn.grading.domain.Grade;

import java.util.Optional;

public interface GradeRepository {
    void save(Grade grade);
    Optional<Grade> findByToken(String token);

    void deleteByToken(String token);

}
