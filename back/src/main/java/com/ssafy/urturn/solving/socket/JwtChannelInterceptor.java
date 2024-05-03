package com.ssafy.urturn.solving.socket;

import com.ssafy.urturn.global.auth.JwtTokenProvider;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * WebSocket 채널에 JWT 검증하는 인터셉터
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 연결 요청시 JWT 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Authorization 헤더 추출
            List<String> authorization = accessor.getNativeHeader(JwtTokenProvider.HEADER);
            if (authorization != null && !authorization.isEmpty()) {
                String token = authorization.get(0).substring(JwtTokenProvider.TOKEN_PREFIX.length());
                log.info("interceptor jwt token : {}", token);
                try {
                    // JWT 토큰 검증
                    if(token == null || !jwtTokenProvider.validateToken(token)) return null;

                    // 유저 정보 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    UserDetails userDetails = (UserDetails) principal;

                    log.info("authentication success");
                    log.info("id : {}", userDetails.getUsername());

                } catch (Exception e) {
                    log.error("An unexpected error occurred: " + e.getMessage());
                    throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR, "소켓 interceptor jwt 인증 에러가 발생하였습니다.");
                }
            } else {
                // 클라이언트 측 타임아웃 처리
                log.error("Authorization header is not found");
                throw new RestApiException(CommonErrorCode.WRONG_REQUEST, "인증헤더를 찾을 수 없습니다");
            }
        }
        return message;
    }
}
