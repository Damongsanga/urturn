package com.ssafy.urturn.global.config;

import com.ssafy.urturn.global.auth.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.global.util.AES128Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtTokenManager jwtTokenManager;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ObjectMapper objectMapper;
    private final AES128Util aes128Util;

    // Spring Security 7.0 부터 삭제될 예정으로, 체이닝 방식에서 람다식으로 변경필요
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 해당 API에 대해서는 모든 요청을 허가. 웹소켓은 필터가 아닌 인터셉터에서 인증
                        .requestMatchers("/ws/**", "/app/**", "/auth/reissue",
                                "/auth/oauth2/login/github", "/auth/test/login", "/actuator/**",
                                "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/check").permitAll()
                        // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                        .anyRequest().authenticated())
                // 에러 핸들링
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler);
                })
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenManager, aes128Util), UsernamePasswordAuthenticationFilter.class)
                // JwtException 핸들링을 위한 Exception 필터
                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class)
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // Jwt 기반임으로 시큐리티의 기본 로그아웃 기능 불필요
                .logout(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // error endpoint를 열어줘야 함, favicon.ico 추가!
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }


}