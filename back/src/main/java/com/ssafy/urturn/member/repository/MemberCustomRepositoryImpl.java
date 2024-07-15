package com.ssafy.urturn.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.urturn.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ssafy.urturn.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByIdAndHasGithubRepository(Long id) {
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(member)
                .where(member.id.eq(id).and(member.githubUniqueId.isNotNull()))
                .fetchFirst(); // == lmit(1).fetchOne()

        return fetchOne != null;
    }
}
