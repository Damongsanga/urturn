package com.ssafy.urturn.global.auth.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.auth.JwtToken;
import com.ssafy.urturn.global.auth.JwtTokenProvider;
import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.LoginReqeust;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.repository.JwtRedisRepository;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.util.AES128Util;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final OAuthClient githubOAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtRedisRepository jwtRedisRepository;
    private final AES128Util aes128Util;

    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;


    @Transactional
    public LoginResponse createAndLogin(LoginReqeust req) {
        if (!memberRepository.existsByNickname(req.getNickname())){
            Member member = Member.builder()
                .nickname(req.getNickname())
                .password(passwordEncoder.encode(req.getPassword() + salt))
                .profileImage("https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/UserProfileImage/baseImage.jpg")
                .githubAccessToken("githubAccessToken")
                .email("damongsanga@email.com")
                .level(Level.LEVEL1)
                .roles(List.of(Role.USER)).build();
            memberRepository.save(member);
        }
        return login(req);
    }

    private LoginResponse login(LoginReqeust req) {
        Member member = memberRepository.findByNickname(req.getNickname()).orElseThrow(() -> new RestApiException(NO_MEMBER));

        log.info("memberId : {}", member.getId());
        JwtToken jwtToken = makeJwtToken(member.getId().toString(), req.getPassword());

        return LoginResponse.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .profileImage(member.getProfileImage())
            .jwtToken(jwtToken)
            .build();
    }

    private JwtToken makeJwtToken(String memberId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password+salt);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }
}
