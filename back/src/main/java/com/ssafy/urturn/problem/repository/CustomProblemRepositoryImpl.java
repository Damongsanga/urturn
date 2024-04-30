package com.ssafy.urturn.problem.repository;

import static com.ssafy.urturn.problem.entity.QMemberProblem.memberProblem;
import static com.ssafy.urturn.problem.entity.QProblem.problem;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.entity.Problem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
@RequiredArgsConstructor
@Repository
public class CustomProblemRepositoryImpl implements CustomProblemRepository{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Problem> selectTwoProblemByLevel(Level level, Long memberId, Long pairId) {

        Long count = jpaQueryFactory
            .select(problem.count())
            .from(problem)
            .leftJoin(problem.memberProblems, memberProblem)
            .where(memberProblem.member.id.ne(memberId)
                .and(memberProblem.member.id.ne(pairId))
                .and(problem.level.eq(level))).fetchOne();

        JPAQuery<Problem> query = jpaQueryFactory
            .select(problem)
            .from(problem)
            .leftJoin(problem.memberProblems, memberProblem)
            .where(problem.level.eq(level));

        // 나와 페어 둘다 풀지 않은 문제가 2개 미만이라면 전체에서 랜덤하게 문제 2개 선정
        if (count == null || count < 2){
            try {
                return query
                    .orderBy(getRandomOrderSpecifier()).limit(2).fetch();
            } catch (Exception e){
                throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR, "문제 선정에 에러가 발행하였습니다.");
            }
        } else { // 아니면 나와 페어 둘다 풀지 않은 문제 2개 선정
            return query
                .where(memberProblem.member.id.ne(memberId)
                    .and(memberProblem.member.id.ne(pairId)))
                .orderBy(getRandomOrderSpecifier()) //mysql 종속적
                .limit(2).fetch();
        }
    }

    private OrderSpecifier<Double> getRandomOrderSpecifier() {
        return Expressions.numberTemplate(Double.class, "RAND()").asc();
    }
}
