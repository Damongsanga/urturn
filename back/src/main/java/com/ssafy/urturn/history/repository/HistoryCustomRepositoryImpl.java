package com.ssafy.urturn.history.repository;

import static com.ssafy.urturn.history.entity.QHistory.history;
import static com.ssafy.urturn.member.entity.QMember.member;
import static com.ssafy.urturn.problem.entity.QProblem.problem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.history.dto.HistoryResponse;
import com.ssafy.urturn.history.entity.QHistory;
import com.ssafy.urturn.member.dto.MemberSimpleDto;
import com.ssafy.urturn.member.dto.ProblemSimpleDto;
import com.ssafy.urturn.member.entity.QMember;
import com.ssafy.urturn.problem.entity.QProblem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<HistoryResponse> getHistories(Long memberId, Pageable pageable) {
        QProblem problem1 = new QProblem("problem1");
        QProblem problem2 = new QProblem("problem2");

        List<HistoryResponse> content = jpaQueryFactory
            .select(Projections.constructor(
                HistoryResponse.class,
                history.id,
                Projections.constructor(MemberSimpleDto.class,
                    member.id,
                    member.nickname,
                    member.profileImage
                    ),
                Projections.constructor(ProblemSimpleDto.class,
                    problem1.id,
                    problem1.title,
                    problem1.level),
                Projections.constructor(ProblemSimpleDto.class,
                    problem2.id,
                    problem2.title,
                    problem2.level),
                history.code1,
                history.code2,
                history.totalRound,
                history.createdAt,
                history.endTime
            ))
            .from(history)
            .leftJoin(history.pair, member)
            .leftJoin(history.problem1, problem1)
            .leftJoin(history.problem2, problem2)
            .where(history.manager.id.eq(memberId))
            .orderBy(history.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = jpaQueryFactory.select(history.count())
            .from(history)
            .where(history.manager.id.eq(memberId))
            .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
