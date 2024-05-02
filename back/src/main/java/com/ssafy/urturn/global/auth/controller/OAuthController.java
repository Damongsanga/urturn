package com.ssafy.urturn.global.auth.controller;


import com.ssafy.urturn.global.auth.dto.LoginResponse;
import com.ssafy.urturn.global.auth.service.OAuthService;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @GetMapping("/oauth2/token")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String code){
        log.info("code : {}", code);
        oAuthService.refreshAccessToken(code);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth2/login/github")
    public ResponseEntity<LoginResponse> gitHubLogin(@RequestParam String code){
        log.info("code : {}", code);
        LoginResponse res = oAuthService.githubOAuthLogin(code);
        HttpHeaders headers = getHeadersWithCookie(res);
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        oAuthService.logout();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) throw new RestApiException(CommonErrorCode.WRONG_REQUEST, "쿠키가 존재하지 않습니다");

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")){
                String encryptedRefreshToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                String newAccessToken = oAuthService.reissueAccessToken(encryptedRefreshToken);
                return ResponseEntity.ok(newAccessToken);
            }
        }

        return new ResponseEntity<String>("필요한 쿠키가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
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
