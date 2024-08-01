package com.ssafy.urturn.global.auth.service;


import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.auth.dto.JwtToken;
import com.ssafy.urturn.global.auth.JwtTokenManager;
import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.auth.dto.OAuthMemberInfoResponse;
import com.ssafy.urturn.global.auth.repository.JwtRedisRepository;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.KeyUtil;
import com.ssafy.urturn.global.util.SecurityUtil;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OAuthService {

    private final JwtTokenManager jwtTokenManager;
    private final MemberRepository memberRepository;
    private final OAuthClient githubOAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtRedisRepository jwtRedisRepository;

    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;

    private static final String DEFAULT_EMAIL = "urturn@urturn.com";


    @Transactional
    public LoginResponse joinAndLogin(String code) {
        OAuthMemberInfoResponse res = getGithubUserInfo(code);
        joinIfNewMember(res);

        Member member = memberRepository.findByGithubUniqueId(res.getOauthId()).orElseThrow(() -> new RestApiException(NO_MEMBER));
        JwtToken jwtToken = createJwtToken(member);

        return LoginResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .jwtToken(jwtToken)
                .build();
    }

    public void logout(){
        Long memberId = SecurityUtil.getMemberId();
        jwtRedisRepository.delete(KeyUtil.getRefreshTokenKey(memberId.toString()));
    }

    // githubUniqueId 반환
    @Transactional
    public String refreshAccessToken(String code){
        OAuthMemberInfoResponse res = getGithubUserInfo(code);
        Member member = memberRepository.findByGithubUniqueId(res.getOauthId()).orElseThrow(() -> new RestApiException(NO_MEMBER));
        member.updateGithubTokens(res.getAccessToken());
        return res.getOauthId();
    }

    private OAuthMemberInfoResponse getGithubUserInfo(String code) {
        try {
            OAuthAccessTokenResponse tokenResponse = githubOAuthClient.getAccessToken(code);
            return githubOAuthClient.getMemberInfo(tokenResponse);
        } catch (HttpClientErrorException e) {
            throw new RestApiException(CustomErrorCode.GITHUB_AUTHORIZATION_ERROR);
        }
    }

    private JwtToken createJwtToken(Member member) {
        return jwtTokenManager.generateToken(new UsernamePasswordAuthenticationToken(
                member.getId().toString(), null, member.getAuthorities()));
    }

    private void joinIfNewMember(OAuthMemberInfoResponse res) {
        if (!memberRepository.existsByNickname(res.getName())){
            Member member =
                Member.builder()
                    .profileImage(res.getProfileUrl())
                    .githubAccessToken(res.getAccessToken())
                    .githubUniqueId(res.getOauthId())
                    .nickname(res.getName())
                    .email(res.getEmail()== null ? DEFAULT_EMAIL : res.getEmail())
                    .roles(List.of(Role.USER))
                    .password(passwordEncoder.encode(res.getName() + salt))
                    .level(Level.LEVEL1)
                    .build();
            memberRepository.save(member);
        }
    }

}
