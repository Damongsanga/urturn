package com.ssafy.urturn.member.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.dto.MemberDetailResponse;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDetailResponse getCurrentMember() {
        Long currentMemberId = MemberUtil.getMemberId();
        Member currentMember = memberRepository.findById(currentMemberId).orElseThrow(() ->  new RestApiException(
            CustomErrorCode.NO_MEMBER));
        return MemberDetailResponse.makeResponse(currentMember);
    }

    @Transactional
    public void updateGithubRepository(String repository) {
        Long currentMemberId = MemberUtil.getMemberId();
        Member currentMember = memberRepository.findById(currentMemberId).orElseThrow(() ->  new RestApiException(
            CustomErrorCode.NO_MEMBER));
        currentMember.updateGithubRepository(repository);
    }


}
