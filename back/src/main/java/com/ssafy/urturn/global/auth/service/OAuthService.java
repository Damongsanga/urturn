package com.ssafy.urturn.global.auth.service;


import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.auth.JwtToken;
import com.ssafy.urturn.global.auth.JwtTokenProvider;
import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.auth.dto.OAuthMemberInfoResponse;
import com.ssafy.urturn.global.auth.repository.JwtRedisRepository;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.AES128Util;
import com.ssafy.urturn.global.util.KeyUtil;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final OAuthClient githubOAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtRedisRepository jwtRedisRepository;


    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;


    @Transactional
    public LoginResponse githubOAuthLogin(String code) {
        OAuthMemberInfoResponse res = getGithubUserInfo(code);
        log.info("res.getOauthId() : {}", res.getOauthId());
        log.info("res.getName() : {}", res.getName());
        log.info("res.getEmail() : {}", res.getEmail());
        log.info("res.getProfileUrl() : {}", res.getProfileUrl());
        log.info("res.getAccessToken() : {}", res.getAccessToken());
        createIfNewMember(res);
        return login(res.getOauthId());
    }

    @Transactional
    public void logout(){
        Long memberId = MemberUtil.getMemberId();
        jwtRedisRepository.delete(KeyUtil.getRefreshTokenKey(memberId.toString()));
    }

    @Transactional
    public void refreshAccessToken(String code){
        OAuthMemberInfoResponse res = getGithubUserInfo(code);
        updateAccessToken(res);
    }

    // memberId로 조회하는 것으로 수정 필요
    private void updateAccessToken(OAuthMemberInfoResponse res){
//        Long memberId = MemberUtil.getMemberId();
        Member member = memberRepository.findByGithubUniqueId(res.getOauthId()).orElseThrow(() -> new RestApiException(NO_MEMBER));
        member.updateGithubTokens(res.getAccessToken());
    }


    private LoginResponse login(String githubUniqueId) {
        Member member = memberRepository.findByGithubUniqueId(githubUniqueId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        log.info("id : {}", member.getId());
        log.info("role : {}", member.getRoles());
        log.info("githubAccessToken : {}", member.getGithubAccessToken());

        JwtToken jwtToken = makeJwtToken(member.getId().toString(), member.getNickname());

        return LoginResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .jwtToken(jwtToken)
                .build();
    }

    private OAuthMemberInfoResponse getGithubUserInfo(String code) {
        try {
            OAuthAccessTokenResponse tokenResponse = githubOAuthClient.getAccessToken(code);
            return githubOAuthClient.getMemberInfo(tokenResponse);
        } catch (HttpClientErrorException e) {
            throw new RestApiException(CustomErrorCode.GITHUB_AUTHORIZATION_ERROR);
        }
    }


    // githubToken으로 비밀번호 대싱 사용
    private JwtToken makeJwtToken(String memberId, String nickname) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, nickname + salt);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    private void createIfNewMember(OAuthMemberInfoResponse res) {
        log.info("access token : {}", res.getAccessToken());
        if (!memberRepository.existsByNickname(res.getName())){
            Member member =
                Member.builder()
                    .profileImage(res.getProfileUrl())
                    .githubAccessToken(res.getAccessToken())
                    .githubUniqueId(res.getOauthId())
                    .nickname(res.getName())
                    .email(res.getEmail())
                    .roles(List.of(Role.USER))
                    .password(passwordEncoder.encode(res.getName() + salt))
                    .level(Level.LEVEL1)
                    .build();
            memberRepository.save(member);
        }
    }

}
