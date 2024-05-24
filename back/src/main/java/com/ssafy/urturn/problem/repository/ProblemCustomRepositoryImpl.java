package com.ssafy.urturn.problem.repository;

import static com.ssafy.urturn.problem.entity.QMemberProblem.memberProblem;
import static com.ssafy.urturn.problem.entity.QProblem.problem;
import static com.ssafy.urturn.problem.entity.QTestcase.testcase;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.entity.Testcase;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ProblemCustomRepositoryImpl implements ProblemCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ProblemTestcaseDto> getProblemAndTestcase(Long problemId) {

        return Optional.ofNullable(jpaQueryFactory.select(problem)
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
                            testcase.stdin,
                            testcase.expectedOutput)
                        ))
                    )
                )).get(problemId));
    }

    @Override
    public Optional<ProblemTestcaseDto> getProblemWithPublicTestcase(Long problemId) {

        return Optional.ofNullable(jpaQueryFactory.select(problem)
            .from(problem)
            .leftJoin(problem.testcases, testcase)
            .where(problem.id.eq(problemId).and(testcase.isPublic.isTrue()))
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
                            testcase.stdin,
                            testcase.expectedOutput)
                        ))
                    )
                )).get(problemId));

    }

    @Override
    public List<ProblemTestcaseDto> selectTwoProblemByLevel(Level level, Long memberId, Long pairId) {

        Long count = jpaQueryFactory
            .select(problem.count())
            .from(problem)
            .leftJoin(problem.memberProblems, memberProblem)
            .where(memberProblem.member.id.ne(memberId)
                .and(memberProblem.member.id.ne(pairId))
                .and(problem.level.eq(level)))
            .fetchOne();

        JPAQuery<Long> query = jpaQueryFactory
            .select(problem.id)
            .from(problem)
            .leftJoin(problem.memberProblems, memberProblem)
            .where(problem.level.eq(level));

        List<Long> problemIds;

        // 나와 페어 둘다 풀지 않은 문제가 2개 미만이라면 전체에서 랜덤하게 문제 2개 선정
        if (count == null || count < 2){
            try {
                problemIds = query.groupBy(problem)
                    .orderBy(getRandomOrderSpecifier()).limit(2).fetch();
            } catch (Exception e){
                throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR, "문제 선정에 에러가 발행하였습니다.");
            }
        } else { // 아니면 나와 페어 둘다 풀지 않은 문제 2개 선정
            problemIds = query
                .where(memberProblem.member.id.ne(memberId)
                    .and(memberProblem.member.id.ne(pairId)))
                .groupBy(problem)
                .orderBy(getRandomOrderSpecifier()) //mysql 종속적
                .limit(2).fetch();
        }

        List<ProblemTestcaseDto> res = new ArrayList<>();
        res.add(getProblemWithPublicTestcase(problemIds.get(0)).orElseThrow(()-> new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR)));
        res.add(getProblemWithPublicTestcase(problemIds.get(1)).orElseThrow(()-> new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR)));

        return res;

    }

    private OrderSpecifier<Double> getRandomOrderSpecifier() {
        return Expressions.numberTemplate(Double.class, "RAND()").asc();
    }
}
