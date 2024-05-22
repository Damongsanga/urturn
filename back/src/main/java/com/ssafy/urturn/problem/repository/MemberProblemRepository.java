package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.problem.entity.MemberProblem;
import com.ssafy.urturn.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProblemRepository  extends JpaRepository<MemberProblem, Long> {

}
