package com.ssafy.urturn.global.auth;

import com.ssafy.urturn.global.util.AES128Util;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AES128Util aes128Util;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String accessToken = jwtTokenProvider.resolveToken(request);
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);

        log.info("getRequestURI : {}", request.getRequestURI());
        log.info("getRequestURL : {}", request.getRequestURL());

        if (accessToken != null){
            if (jwtTokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
            }
            else if (!jwtTokenProvider.validateToken(accessToken) && encryptedRefreshToken != null){
                String refreshToken = aes128Util.decryptAes(encryptedRefreshToken);
                if (jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.existsRefreshToken(refreshToken)){
                    String newAccessToken = jwtTokenProvider.generateNewAccessToken(refreshToken);
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                    this.setAuthentication(newAccessToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
