package com.ssafy.urturn.grading.mock;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeGradeRepository implements GradeRepository {

    private final Map<String, Grade> data = new HashMap<>();

    @Override
    public void save(Grade grade) {
        if (data.containsKey(grade.getToken())) throw new DuplicateKeyException("이미 존재하는 토큰에 대한 삽입요청입니다.");
        data.put(grade.getToken(), grade);
    }

    @Override
    public Optional<Grade> findByToken(String token) {
        return Optional.ofNullable(data.get(token));
    }

    @Override
    public void deleteByToken(String token) {
        return;
    }
}
