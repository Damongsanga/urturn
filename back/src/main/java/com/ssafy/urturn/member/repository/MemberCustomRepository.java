package com.ssafy.urturn.member.repository;

public interface MemberCustomRepository {


    boolean existsByIdAndHasGithubRepository(Long id);

}
