package com.ssafy.urturn.webrtc.controller;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.KurentoOptions;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sessions")
@RestController
public class OpenviduController {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @PostMapping("")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
        throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);

        // 세션 만들면서 동시에 해야하는 로직

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
    public ResponseEntity<Map> createConnection(@PathVariable("sessionId") String sessionId,
        @RequestBody(required = false) Map<String, Object> params)
        throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Connection 필터에 필요한 것을 제공
//        ConnectionProperties properties = new ConnectionProperties.Builder().kurentoOptions(
//            new KurentoOptions.Builder()
//                .allowedFilters(new String[]{"GStreamerFilter", "FaceOverlayFilter", "ChromaFilter"})
//                .build()).build();
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
