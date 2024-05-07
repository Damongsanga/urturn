package com.ssafy.urturn.member.repository;


import com.ssafy.urturn.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findByGithubUniqueId(String uniqueId);

    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
    boolean existsByGithubAccessToken(String githubToken);
}
