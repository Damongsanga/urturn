package com.ssafy.urturn.solving.socket;


import com.ssafy.urturn.global.util.MemberUtil;
import java.util.List;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.solving.cache.cacheDatas;
import com.ssafy.urturn.solving.dto.*;
import com.ssafy.urturn.solving.service.RoomService;
import com.ssafy.urturn.solving.temp.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import static com.ssafy.urturn.global.util.JsonUtil.convertObjectToJson;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomService roomService;
    private final MemberService memberService;
    private final cacheDatas cachedatas;
    // 사용자가 데이터를 app/hello 경로로 데이터 날림.
    // 클라이언트는 /topic/greetings 주제를 구독하고 서버에서 이 주제로 메시지가 발행되면 이를 수신.





    @MessageMapping("/test")
    public void test(){
        System.out.println("test");
        simpMessagingTemplate.convertAndSendToUser(MemberUtil.getMemberId().toString(),"/queue/test","test");
        // user/{userId}/queue/test
    }

    @MessageMapping("/createRoom")
    public void createRoom(@Payload memberIdDto memberIddto) {
        cachedatas.clearAllCache();
        Long userId=memberIddto.getMemberId();
        roomInfoResponse roomInfoResponse = roomService.createRoom(userId);
        userInfoResponse userInfoResponse = roomService.getUserInfo(userId,null);
//        // response에 포함된 방 정보를 이용하여 방을 생성한 사용자에게만 응답을 보냄
        simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/roomInfo", roomInfoResponse);
        simpMessagingTemplate.convertAndSendToUser(userId.toString(),"/userInfo",userInfoResponse);
    }

    @MessageMapping("/enterRoom")
    public void enterRoom(@Payload roomIdDto roomIddto) {

        String roomId=roomIddto.getRoomId();
        Long participantId=cachedatas.cacheroomInfoDto(roomId).getParticipantId();
        Long managerId=cachedatas.cacheroomInfoDto(roomId).getManagerId();
        userInfoResponse userInfoResponse=roomService.getUserInfo(participantId,managerId);

        roomInfoResponse roomInfoResponse=new roomInfoResponse(roomId,null, false);

        simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "/roomInfo", roomInfoResponse);
        simpMessagingTemplate.convertAndSendToUser(participantId.toString(),"/userInfo",userInfoResponse);
        userInfoResponse userInfoResponse2=roomService.getUserInfo(managerId,participantId);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(),"/userInfo",userInfoResponse2);
    }


    @MessageMapping("/selectDifficulty")
    public void readContext(@Payload selectDifficultyRequest selectDifficultyRequest){


        // 두 유저ID 추출
        Long participantId=cachedatas.cacheroomInfoDto(selectDifficultyRequest.getRoomId()).getParticipantId();
        Long managerId=cachedatas.cacheroomInfoDto(selectDifficultyRequest.getRoomId()).getManagerId();

        algoQuestionResponse[] algoQuestions=roomService.getAlgoQuestion(selectDifficultyRequest.getRoomId(), selectDifficultyRequest.getDifficulty());

        simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "/questionInfo",algoQuestions);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(),"/questionInfo",algoQuestions);

    }

    @MessageMapping("/readyToSolve")
    public synchronized void readyToSolve(@Payload readyInfoRequest readyInfoRequest){

        System.out.println("readyToSolve");
        Long participantId=cachedatas.cacheroomInfoDto(readyInfoRequest.getRoomId()).getParticipantId();
        Long managerId=cachedatas.cacheroomInfoDto(readyInfoRequest.getRoomId()).getManagerId();

        if(roomService.setReadyInRoomInfo(readyInfoRequest)){
            /*
             여기 History Table에 데이터 삽입 로직 구현 (삽입 데이터 : 아이디. 방장, 팀원, 첫번째문제, 두번째문제.)
             서비스 단 호출해서 구현 시 RoomService에 메서드 만들어서 호출해 주세용

             DB에 problem 테이블이랑 testcase 테이블에 더미 데이터 존재해야함.

             */

            // 여기 방장, 팀원, 문제1, 문제2 데이터 저장되어 있음.
            roomInfoDto roomInfoDto=cachedatas.cacheroomInfoDto(readyInfoRequest.getRoomId());

            simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "/startToSolve",true);
            simpMessagingTemplate.convertAndSendToUser(managerId.toString(),"/startToSolve",true);
        }

    }

    @MessageMapping("/switchCode")
    public synchronized void switchCode(@Payload switchCodeRequest switchCodeRequest){
        // 방 ID, 문제 ID 확인해 코드 스냅샷 저장.


        cachedatas.updateCodeCache(switchCodeRequest.getRoomId(),switchCodeRequest.getAlgoQuestionId().toString(),
                new userCodeDto(switchCodeRequest.getRound(),switchCodeRequest.getCode()));

        Long participantId=cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getParticipantId();
        Long managerId=cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).getManagerId();
        System.out.println("시작 부 host : "+ switchCodeRequest.isHost()+" managerIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isManagerIsReady()+" " +
                "partcipantIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isParticipantIsReady());

        // 준비완료.
        roomService.updateReadyStatus(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()), switchCodeRequest.isHost());

        System.out.println("host : "+ switchCodeRequest.isHost()+" managerIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isManagerIsReady()+" " +
                "partcipantIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isParticipantIsReady());
        // 준비상태 확인
        if(roomService.areBothParticipantsReady(switchCodeRequest.getRoomId(), cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()))){
            if(switchCodeRequest.isHost()){
                System.out.println("Data 1");
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(),"/switchCode",roomService.getParticipantsCode(switchCodeRequest));
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "/switchCode",new switchCodeResponse(switchCodeRequest.getCode(),switchCodeRequest.getRound()+1));
            }else{
                System.out.println("Data 2");
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(),"/switchCode",roomService.getParticipantsCode(switchCodeRequest));
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/switchCode",new switchCodeResponse(switchCodeRequest.getCode(),switchCodeRequest.getRound()+1));
            }
        }
        System.out.println("모두 false 여야함    host : "+ switchCodeRequest.isHost()+" managerIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isManagerIsReady()+" " +
                "partcipantIsReady : "+cachedatas.cacheroomInfoDto(switchCodeRequest.getRoomId()).isParticipantIsReady());
    }

    @MessageMapping("/submitCode")
    public void submitCode(@Payload submitRequest submitRequest){

        Long participantId=cachedatas.cacheroomInfoDto(submitRequest.getRoomId()).getParticipantId();
        Long managerId=cachedatas.cacheroomInfoDto(submitRequest.getRoomId()).getManagerId();

        // 구현해야 하는 부분
        submitResponse submitResponse = roomService.submitCode(submitRequest);

        if(submitResponse.isResult()) {
            //정답 일 경우.
            if (submitRequest.isHost()) {
                // 사용자에게 정답 응답 및 메시지 전송.
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "submit/result", submitResponse);
                // 일단은 역할을 data를 String(""Navigator")으로 넘기지만, Enum, dto형태든 원하는 형태로 수정 가능.
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "role", "Navigator");
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "role", "Driver");
            } else {
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "submit/result", submitResponse);
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "role", "Navigator");
                simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "role", "Driver");
            }
        } else{
            // 오답 일 경우
            simpMessagingTemplate.convertAndSendToUser(participantId.toString(), "submit/result", submitResponse);
        }

    }

    @MessageMapping("/switchRole")
    public void switchRole(@Payload switchCodeRequest switchCodeRequest){

    }

}
/*
convertAndSendToUser(String user, String destination, Object payload)
user : 메시지를 받을 사용자의 식별자. 고유ID
destination : 메시지를 보낼 목적지(경로) -> 클라이언트가 메시지를 수신하기 위해 구독하는 경로.
payload 전송할 메시지의 내용

목적지 /queue/roomCreated 는  /user/{userId}/queue/roomCreated로 변환 됨.
이때 /user라는 접두사는 Spring 메시지 브로커가 메시지를 해당 사용자의 세션에 자동으로 라우팅하는데 사용 표준 접두사
{userId} 사용자를 식별하는 고유 키 인증 과정에서 얻어지며, Principal 객체의 이름 또는 특정 필드로부터 추출.

자동 처리: 사용자가 직접 /user/{userId}/queue/... 형태의 경로를 구독할 필요는 없습니다.
클라이언트는 보다 일반적인 경로(예: /queue/roomCreated)를 구독하고,
Spring 웹소켓 인프라가 자동으로 /user/{userId}/queue/roomCreated 형태로 변환하여 해당 사용자의 메시지만을 받도록 합니다.

사용자 ID 전달: 일반적으로 userId는 서버가 사용자의 인증 정보에서 자동으로 추출합니다.

response는 서버에서 내부 로직 처리 후 클라이언트에게 보낼 객체.
 */