package com.ssafy.urturn.problem.repository;

import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.entity.Problem;
import java.util.List;

public interface CustomProblemRepository {

    public List<Problem> selectTwoProblemByLevel(Level level, Long memberId, Long pairId);
}
