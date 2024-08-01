package com.ssafy.urturn.global.auth.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.global.auth.dto.JwtToken;
import com.ssafy.urturn.global.auth.JwtTokenManager;
import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.LoginRequest;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder;

    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;

    private static final String DEFAULT_IMAGE_URL = "https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/UserProfileImage/baseImage.jpg";
    private static final String DEFAULT_EMAIL = "default@email.com";
    private static final String DEFAULT_GITHUB_TOKEN = "githubAccessToken";


    @Transactional
    public LoginResponse joinAndLogin(LoginRequest req) {
        joinIfNewMember(req);

        Member member = memberRepository.findByNickname(req.getNickname()).orElseThrow(() -> new RestApiException(NO_MEMBER));
        authenticateLoginRequest(req, member);

        JwtToken jwtToken = createJwtToken(member);

        return LoginResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .jwtToken(jwtToken)
                .build();
    }

    private void joinIfNewMember(LoginRequest req) {
        if (!memberRepository.existsByNickname(req.getNickname())) {
            Member member = Member.builder()
                    .nickname(req.getNickname())
                    .password(passwordEncoder.encode(req.getPassword() + salt))
                    .profileImage(DEFAULT_IMAGE_URL)
                    .githubAccessToken(DEFAULT_GITHUB_TOKEN)
                    .email(DEFAULT_EMAIL)
                    .level(Level.LEVEL1)
                    .roles(List.of(Role.USER)).build();
            memberRepository.save(member);
        }
    }

    private void authenticateLoginRequest(LoginRequest req, Member member) {
        if (!passwordEncoder.matches(req.getPassword()+ salt, member.getPassword()))
            throw new RestApiException(CustomErrorCode.WRONG_PASSWORD);
    }

    private JwtToken createJwtToken(Member member) {
        return jwtTokenManager.generateToken(new UsernamePasswordAuthenticationToken(
                member.getId().toString(), null, member.getAuthorities()));
    }
}
