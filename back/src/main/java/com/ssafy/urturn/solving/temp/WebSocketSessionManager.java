package com.ssafy.urturn.solving.temp;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class WebSocketSessionManager {

    // 사용자 ID 별로 웹소켓 세션을 저장하는 맵.
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 특정 사용자 ID에 대한 웹소켓 세션을 등록하는 메서드.
    public void registerSession(Long userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    // 사용자 ID와 연결된 웹소켓 세션을 등록하는 메서드.
    public void removeSession(Long userId) {
        sessions.remove(userId);
    }



    public void sendMessage(Long userId, String message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.err.println("Failed to send message to user " + userId + ": " + e.getMessage());
                // Handle exceptions, possibly close session
            }
        } else {
            System.err.println("No active session for user ID " + userId);
        }
    }
}
