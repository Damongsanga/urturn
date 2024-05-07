package com.ssafy.urturn.solving.temp;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


/*
웹소켓 연결 전과 후의 처리 담당

beforeHandshake에서 attributes
beforeHandshake 메서드에서 사용하는 맵.
웹소켓 핸드셰이크 동안 HTTP 요청과 연결된 웹소켓 세션에 데이터를 추가 할 수 있도록 하는 저장소.
이 맵은 핸드셰이크 과정에서 설정된 속성들을 웹소켓 세션 생성 후에도 사용할 수 있게 해줌.

용도
사용자 식별 정보 저장 : 인증 과정을 거친 사용자의 ID를 맵에 저장함으로써, 웹소켓 세션이 해당 사용자를 식별 할 수 있음.
이는 메시지를 사용자별로 라우팅하거나, 사용자 권한을 확인하는데 중요.
attributes 핸드셰이크 요청 당 한 번 생성, 각각의 웹소켓 핸드셰이크 요청과 연결되 웹 소켓 세션별로 별도 관리.
따라서 각 사용자의 요청은 고유한 "attributes" 맵을 가지게 되고, 이를 통해 사용바별 세션 데이터를 독립적으로 관리 할 수 있음.
 */

@RequiredArgsConstructor
@Component
public class MemberIdHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // SecurityContextHolder를 통해 사용자 ID를 추출하고, 웹소켓 세션 속성에 추가
        try {
            Long memberId = MemberUtil.getMemberId();  // 사용자 ID를 추출
            attributes.put("memberId", memberId);  // 웹소켓 세션 속성에 사용자 ID 저장    -> 그럼 세션은 사용자 별로 있는건지?
            return true;  // 핸드셰이크를 계속 진행
        } catch (RestApiException e) {
            System.out.println("멤버 아이디 추출에 실패 하였습니다. " + e.getMessage());
            return false;  // 핸드셰이크 실패 처리
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception == null) {
            // 핸드셰이크가 성공적으로 완료된 경우
            System.out.println("핸드셰이크 성공");
        } else {
            // 핸드셰이크 과정에서 예외가 발생한 경우
            System.out.println("핸드셰이크 도중 에러 발생 : " + exception.getMessage());

            // 핸드셰이크 실패 시 필요한 에러 처리 로직을 추가할 수 있습니다.

        }
    }


    /*
     웹소켓 핸드셰이크 과정이 완료된 후 호출. 이 메서드는 핸드셰이크의 성공 여부를 로그로 남김.
     Exception 파라미터를 확인하여 핸드셰이크 과정에서 예외가 발생했는지 확인.
     예외가 없으면 성공적으로 핸드셰이크가 완료되었음을 로그에 기록.
     질문? Exception은 따로 구현??
     */




}
