package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemCustomRepository {

    Optional<Problem> findByTitle(String title);

    boolean existsByTitle(String title);


}
