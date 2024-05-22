package com.ssafy.urturn.webrtc.controller;

import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/sessions")
@RestController
public class OpenviduController {

    @Value("${OPENVIDU_URL}")
    private String openviduUrl;

    @Value("${OPENVIDU_SECRET}")
    private String openviduSecret;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(openviduUrl, openviduSecret);
    }

    @PostMapping("")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
        throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);
        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }

    @DeleteMapping("{sessionId}")
    public ResponseEntity<Void> deleteSession(
        @PathVariable("sessionId") String sessionId)
        throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        session.close();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{sessionId}/connection")
    public ResponseEntity<Map<String, String>> createConnection(@PathVariable("sessionId") String sessionId,
        @RequestBody(required = false) Map<String, Object> params)

        throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();

        Connection connection = session.createConnection(properties);
        Map<String, String> connectionInfo = new HashMap<>();
        connectionInfo.put("token", connection.getToken());
        connectionInfo.put("connectionId", connection.getConnectionId());

        return new ResponseEntity<>(connectionInfo, HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}/connection/{connectionId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable("sessionId") String sessionId,
        @PathVariable("connectionId") String connectionId)

        throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        session.forceDisconnect(connectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
