package com.ssafy.urturn.grading.infrastructure;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GradeRepositoryImpl implements GradeRepository {

    private final GradeRedisRepository gradeRedisRepository;

    @Override
    public void save(Grade grade){
        gradeRedisRepository.save(GradeEntity.from(grade));
    }

    @Override
    public Optional<Grade> findByToken(String token){
        return gradeRedisRepository.findByToken(token).map(GradeEntity::toModel);
    }

    @Override
    public void deleteByToken(String token) {
        gradeRedisRepository.deleteByToken(token);
    }

    @Override
    public void deleteAll(){
        gradeRedisRepository.deleteAll();
    }
}
