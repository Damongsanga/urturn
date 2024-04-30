package com.ssafy.urturn.problem.service;

import com.ssafy.urturn.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
