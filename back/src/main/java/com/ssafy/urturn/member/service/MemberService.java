package com.ssafy.urturn.member.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.util.SecurityUtil;
import com.ssafy.urturn.member.dto.response.MemberDetailResponse;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDetailResponse getCurrentMember() {
        Long currentMemberId = SecurityUtil.getMemberId();
        Member currentMember = memberRepository.findById(currentMemberId).orElseThrow(() -> new RestApiException(
                NO_MEMBER));
        return MemberDetailResponse.makeResponse(currentMember);
    }

    @Transactional
    public void updateGithubRepository(String repository) {
        Long currentMemberId = SecurityUtil.getMemberId();
        Member currentMember = memberRepository.findByIdForUpdate(currentMemberId).orElseThrow(() -> new RestApiException(
                NO_MEMBER));
        currentMember.updateGithubRepository(repository);
    }

    public boolean hasGithubRepository(Long memberId) {
        return memberRepository.existsByIdAndHasGithubRepository(memberId);
    }



}
