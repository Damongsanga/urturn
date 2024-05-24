package com.ssafy.urturn.global.auth;


import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ReferrerCheckInterceptor implements HandlerInterceptor {

    private final List<String> allowedHost = Arrays.asList("localhost:3000", "localhost:3002", "localhost:3003");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("Referer");
        String host = request.getHeader("Host");
        log.info("referer : {}", referer);
        log.info("host : {}", host);
        if (referer == null || !(referer.contains(host) || allowedHost.contains(host))) {
            throw new RestApiException(CommonErrorCode.WRONG_REQUEST, "referer 헤더 정보가 설정되어있지 않거나 잘못되었습니다");
        }
        return true;
    }
}
