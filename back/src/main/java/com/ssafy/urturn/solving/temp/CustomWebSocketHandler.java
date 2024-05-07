package com.ssafy.urturn.solving.temp;
import org.springframework.web.socket.CloseStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
웹 소켓 핸들로로서, 웹소켓 연결의 생명 주기 이벤트를 관리.
 */
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;

    // 웹소켓 연결이 성공적으로 수립되었을 때 호출. 사용자의 웹소켓 세션을 WebSocketSessionManager을 통해 등록.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓 연결이 성공적으로 수립되면 세션을 관리자에 등록

        // 세션의 속성에서 "memberId" 키를 사용하여 사용자 ID를 가져옴.
        Long memberId = (Long) session.getAttributes().get("memberId");
        if (memberId != null) {
            // 가져온 ID를 사용하여 해당 사용자의 세션을 "sessionManager"에 등록
            sessionManager.registerSession(memberId, session);
        }
    }


    /*
    웹소켓 연결이 종료 되었을 때 호출. 사용자의 웹소켓 세션을 WebSocketSessionManager에서 제거.
    가져온 ID를 사용하여 해당 사용자 세션을 "sessionManager"에서 제거.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 웹소켓 연결이 종료되면 세션을 관리자에서 제거
        Long memberId = (Long) session.getAttributes().get("memberId");
        if (memberId != null) {
            sessionManager.removeSession(memberId);
        }
    }
}
/*
전체적인 동작 흐름
클라이언트가 서버에 웹소켓 연결을 요청합니다.
MemberIdHandshakeInterceptor의 beforeHandshake 메서드가 호출되어 사용자 인증을 수행합니다.
핸드셰이크가 성공적으로 완료되면, CustomWebSocketHandler의 afterConnectionEstablished 메서드가 호출되어 사용자 세션을 등록합니다.
클라이언트와 서버 간의 웹소켓 통신이 이루어집니다.
연결이 종료되면, CustomWebSocketHandler의 afterConnectionClosed 메서드가 호출되어 사용자 세션을 제거합니다.
 */