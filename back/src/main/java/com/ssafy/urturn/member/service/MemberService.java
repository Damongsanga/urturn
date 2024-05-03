package com.ssafy.urturn.member.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.dto.MemberDetailResponse;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import com.ssafy.urturn.solving.dto.userInfoResponse;
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

    public Member getMemberById(Long id){
        return memberRepository.findById(id).orElseThrow(() ->  new RestApiException(
            CustomErrorCode.NO_MEMBER));
    }

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

//    public userInfoResponse getMemberInfo(Long myUserId, Long relativeUserId) {
//        userInfoResponse userInfo = new userInfoResponse();
//        if(myUserId!=null) {
//            Member member = memberRepository.findById(myUserId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//            userInfo.setMyUserProfileUrl(member.getProfileImage());
//            userInfo.setMyUserNickName(member.getNickname());
//        }
//
//        if(relativeUserId!=null) {
//            Member member = memberRepository.findById(relativeUserId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//            userInfo.setRelativeUserProfileUrl(member.getProfileImage());
//            userInfo.setRelativeUserNickName(member.getNickname());
//        }
//
//        return userInfo;
//    }

    public userInfoResponse getMemberInfo(Long myUserId, Long relativeUserId) {
        userInfoResponse userInfo = new userInfoResponse();

        // 사용자 정보 설정
        setUserInfo(userInfo, myUserId, true);
        setUserInfo(userInfo, relativeUserId, false);

        return userInfo;
    }

    // 사용자 정보를 설정하는 보조 메서드
    private void setUserInfo(userInfoResponse userInfo, Long userId, boolean isMyUser) {
        if (userId != null) {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("부적절한 유저 아이디 : " + userId));
            if (isMyUser) {
                userInfo.setMyUserProfileUrl(member.getProfileImage());
                userInfo.setMyUserNickName(member.getNickname());
            } else {
                userInfo.setRelativeUserProfileUrl(member.getProfileImage());
                userInfo.setRelativeUserNickName(member.getNickname());
            }
        }
    }


}
