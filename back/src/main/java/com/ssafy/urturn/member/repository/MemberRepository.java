package com.ssafy.urturn.member.repository;


import com.ssafy.urturn.member.entity.Member;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByGithubUniqueId(String uniqueId);

    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    // 사실 로그인된 유저만 업데이트할 수 있지만 락을 사용해보았다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT a FROM MEMBER a 
            WHERE a.id = :id
            """)
    Optional<Member> findByIdForUpdate(Long id);

}
