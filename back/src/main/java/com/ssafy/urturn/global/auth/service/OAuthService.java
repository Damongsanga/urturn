package com.ssafy.urturn.global.auth.service;


import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.auth.JwtToken;
import com.ssafy.urturn.global.auth.JwtTokenProvider;
import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.auth.dto.OAuthMemberInfoResponse;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
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


    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;


    @Transactional
    public LoginResponse githubOAuthLogin(String code) {
        OAuthMemberInfoResponse res = getGithubUserInfo(code);
        log.info("res.getOauthId() : {}", res.getOauthId());
        log.info("res.getName() : {}", res.getName());
        log.info("res.getEmail() : {}", res.getEmail());
        log.info("res.getProfileUrl() : {}", res.getProfileUrl());
        createIfNewMember(res);
        return login(res.getOauthId());
    }


    private LoginResponse login(String githubToken) {
        Member member = memberRepository.findByGithubToken(githubToken).orElseThrow(() -> new RestApiException(NO_MEMBER));
        log.info("id : {}", member.getId());
        log.info("role : {}", member.getRoles());
        log.info("githubToken : {}", member.getGithubToken());

        JwtToken jwtToken = makeJwtToken(member.getId().toString(), member.getGithubToken());

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
            return githubOAuthClient.getMemberInfo(tokenResponse.getAccessToken());
        } catch (HttpClientErrorException e) {
            throw new RestApiException(CustomErrorCode.GITHUB_AUTHORIZATION_ERROR);
        }
    }

    private JwtToken makeJwtToken(String memberId, String githubToken) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, githubToken + salt);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    private void createIfNewMember(OAuthMemberInfoResponse res) {
        if (!memberRepository.existsByGithubToken(res.getOauthId())){
            Member member =
                Member.builder()
                    .profileImage(res.getProfileUrl())
                    .githubToken(res.getOauthId())
                    .nickname(res.getName()) // 이거 github nickname으로 수정해야함
                    .roles(List.of(Role.USER))
                    .password(passwordEncoder.encode(res.getOauthId() + salt))
                    .level(Level.LEVEL1)
                    .build();
            memberRepository.save(member);
        }
    }

}
