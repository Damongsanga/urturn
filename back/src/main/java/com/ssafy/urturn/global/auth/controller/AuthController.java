package com.ssafy.urturn.global.auth.controller;

import com.ssafy.urturn.global.auth.dto.LoginReqeust;
import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/test")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAndLogin(@RequestBody @Valid LoginReqeust req){
        LoginResponse res = authService.createAndLogin(req);
        HttpHeaders headers = getHeadersWithCookie(res);
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }

    private HttpHeaders getHeadersWithCookie(LoginResponse res) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", res.getJwtToken().deleteRefreshToken())
            .maxAge(refreshTokenValidityInSeconds)
            .path("/")
            .secure(true)
            .sameSite("None")
            .httpOnly(true)
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie.toString());
        return headers;
    }
}
