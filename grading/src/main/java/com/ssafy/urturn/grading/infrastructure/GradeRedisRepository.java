package com.ssafy.urturn.grading.infrastructure;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GradeRedisRepository extends CrudRepository<GradeEntity, String> {
    Optional<GradeEntity> findByToken(String token);
}
