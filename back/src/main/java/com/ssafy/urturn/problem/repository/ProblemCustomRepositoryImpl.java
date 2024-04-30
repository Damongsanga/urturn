package com.ssafy.urturn.problem.repository;

import static com.ssafy.urturn.problem.entity.QProblem.problem;
import static com.ssafy.urturn.problem.entity.QTestcase.testcase;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ProblemCustomRepositoryImpl implements ProblemCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ProblemTestcaseDto getProblemAndTestcase(Long problemId) {

        return jpaQueryFactory.select(problem)
            .from(problem)
            .leftJoin(problem.testcases, testcase)
            .where(problem.id.eq(problemId))
            .distinct()
            .transform(
                groupBy(problem.id).as(Projections.constructor(
                    ProblemTestcaseDto.class,
                    problem.id,
                    problem.title,
                    problem.content,
                    problem.level,
                    list((Projections.constructor(TestcaseDto.class,
                            testcase.id,
                            testcase.language,
                            testcase.stdin,
                            testcase.expectedOutput)
                        ))
                    )
                )).get(problemId);
    }
}
