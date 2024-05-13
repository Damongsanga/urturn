package com.ssafy.urturn.history.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.ssafy.urturn.history.entity.QHistory.history;
import static com.ssafy.urturn.member.entity.QMember.member;
import static com.ssafy.urturn.problem.entity.QProblem.problem;
import static com.ssafy.urturn.problem.entity.QTestcase.testcase;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.history.dto.HistoryResponse;
import com.ssafy.urturn.history.dto.HistoryRetroDto;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.history.entity.QHistory;
import com.ssafy.urturn.member.dto.MemberSimpleDto;
import com.ssafy.urturn.member.dto.ProblemSimpleDto;
import com.ssafy.urturn.member.entity.QMember;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.TestcaseDto;
import com.ssafy.urturn.problem.entity.QProblem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
                history.result,
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

    @Override
    public HistoryRetroDto getMostRecentHistoryByMemberId(Long memberId) {

        QProblem problem1 = new QProblem("problem1");
        QProblem problem2 = new QProblem("problem2");

        QMember manager = new QMember("manager");
        QMember pair = new QMember("pair");

        Long historyId =  Objects.requireNonNull(jpaQueryFactory.selectFrom(history)
            .where(history.manager.id.eq(memberId).or(history.pair.id.eq(memberId)))
            .orderBy(history.endTime.desc())
            .limit(1)
            .fetchOne()).getId();

        return Objects.requireNonNull(jpaQueryFactory.select(history)
            .from(history)
            .leftJoin(history.problem1, problem1)
            .leftJoin(history.problem2, problem2)
            .leftJoin(history.manager, manager)
            .leftJoin(history.pair, pair)
            .where(history.id.eq(historyId))
            .distinct()
            .transform(
                groupBy(history.id).as(Projections.constructor(
                        HistoryRetroDto.class,
                        history.id,
                        history.manager.nickname,
                        history.pair.nickname,
                        history.problem1.title,
                        history.problem2.title,
                        history.problem1.content,
                        history.problem2.content,
                        history.code1,
                        history.code2,
                        history.language1,
                        history.language2,
                    history.retro1,
                    history.retro2,
                    history.totalRound,
                    history.result,
                    history.endTime
                        )
                    )
                )).get(historyId);

    }
}
