package com.ssafy.urturn.member.repository;


import com.ssafy.urturn.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findByGithubToken(String githubToken);
    boolean existsByGithubToken(String githubToken);
}
