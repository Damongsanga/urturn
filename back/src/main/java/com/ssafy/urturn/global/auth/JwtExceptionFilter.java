package com.ssafy.urturn.global.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.exception.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); 
        }
        catch (JwtException e) {
            makeErrorResponse(res);
        }
    }

    private void makeErrorResponse(HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(CustomErrorCode.NOT_VALID_USER.getHttpStatus().value());
        res.getWriter().write(convertObjectToJson(ErrorResponse.builder()
                .code(CustomErrorCode.NOT_VALID_USER.name())
                .message(CustomErrorCode.NOT_VALID_USER.getMessage())
                .build()));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
