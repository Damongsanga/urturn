package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.problem.entity.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
}
