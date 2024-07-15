package com.ssafy.urturn.solving.controller;

import static com.ssafy.urturn.solving.PairProgrammingRole.DRIVER;
import static com.ssafy.urturn.solving.PairProgrammingRole.NAVIGATOR;

import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.solving.dto.request.*;
import com.ssafy.urturn.solving.dto.response.*;
import com.ssafy.urturn.solving.service.SolveService;

import java.util.Map;
import java.util.Optional;

import com.ssafy.urturn.solving.service.SolveWebsocketSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SolveWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SolveService solveService;
    private final SolveWebsocketSerivce solveWebsocketSerivce;
    private final MemberService memberService;

    @MessageMapping("/selectLevel")
    public void readContext(@Payload SelectLevelRequest request) {
        log.info("난이도 선택 및 시작 로직 SelectLevel Dto = {}", request);
        sendTwoProblemTestcases(solveWebsocketSerivce.readContext(request));
    }

    @MessageMapping("/readyToSolve")
    public void readyToSolve(@Payload ReadyInfoRequest request) {
        log.info("문제 풀이 시작 로직 readyInfo Dto = {}", request);
        sendStartSolveRequest(solveWebsocketSerivce.readyToSolve(request));
    }

    @MessageMapping("/switchCode")
    public void switchCode(@Payload SwitchCodeRequest request) {
        log.info("코드 스위치 로직 SwitchCode Dto = {}", request);
        SwitchCodeResponse response = solveWebsocketSerivce.switchCode(request);

        if (response.isEmpty()) return;

        if (response.isFinalRound())
            sendRetroCode(request.getRoomId(), response.getMemberId(), response.getMemberId()); // 회고창으로 전환
        else if (response.isPair())
            sendSwitchRoleRequest(request, response); // 페어 프로그래밍 모드에서 역할 전환 메시지. 코드 전환은 하지 않음
        else
            sendSwitchCode(request, response); // 릴레이 모드에서 코드 전환
    }

    @MessageMapping("/submitCode")
    public void submitCode(@Payload SubmitRequest request) {
        SubmitCodeResponse response = solveWebsocketSerivce.submitCode(request);

        if (!response.isAccepted()) {
            simpMessagingTemplate.convertAndSendToUser(response.isHost() ? response.managerId().toString() : response.pairId().toString(), "/submit/result", response);
            return;
        }

        // 문제를 푼 사람이 호스트 인 경우.
        else if (request.isHost()) {
            // 사용자에게 정답 응답 및 메시지 전송.
            simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/submit/result", response);
            // 현재 페어프로그래밍 모드가 아닌 릴레이모드라면,
            if (!request.isPair()) {
                sendRoleAsNavigatorAndDriver(response.managerId(), response.pairId());
            }
        } else {
            simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/submit/result", response);
            if (!request.isPair()) {
                sendRoleAsNavigatorAndDriver(response.pairId(), response.managerId());
            }
        }

        // 페어프로그래밍 모드 인 경우 retroCode 보여주기
        if (request.isPair()) {
            sendRetroCode(request.getRoomId(), response.managerId(), response.pairId());
        }
    }



    @MessageMapping("/submitRetro")
    public void submitRetro(@Payload RetroCreateRequest request) {
        sendGithubUploadRequest(solveWebsocketSerivce.submitRetro(request));
    }

    private void sendGithubUploadRequest(ManagerPairIdResponse response) {
        simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/finishSocket/githubUpload", memberService.hasGithubRepository(response.managerId()));
        simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/finishSocket/githubUpload", memberService.hasGithubRepository(response.pairId()));
    }

    private void sendTwoProblemTestcases(ReadContextResponse response) {
        simpMessagingTemplate.convertAndSendToUser(response.pairId().toString(), "/questionInfo", response.problemTestcases());
        simpMessagingTemplate.convertAndSendToUser(response.managerId().toString(), "/questionInfo", response.problemTestcases());
        log.info("problemTestcases : {}", response.problemTestcases());
    }

    private void sendStartSolveRequest(Optional<ManagerPairIdResponse> response) {
        response.ifPresent(dto -> {
            simpMessagingTemplate.convertAndSendToUser(dto.pairId().toString(), "/startToSolve", true);
            simpMessagingTemplate.convertAndSendToUser(dto.managerId().toString(), "/startToSolve", true);
        });
    }

    private void sendRoleAsNavigatorAndDriver(Long managerId, Long pairId) {
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/role", NAVIGATOR.getValue());
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/role", DRIVER.getValue());
    }

    private void sendRetroCode(String roomId, Long memberId, Long pairId) {
        // 회고창에서 보여줄 데이터
        Map<Long, RetroCodeResponse> retroCodeMap = solveService.makeRetroCodeResponse(roomId);
        simpMessagingTemplate.convertAndSendToUser(memberId.toString(), "/showRetroCode", retroCodeMap);
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/showRetroCode", retroCodeMap);
    }

    private void sendSwitchRoleRequest(SwitchCodeRequest request, SwitchCodeResponse response) {
        simpMessagingTemplate.convertAndSendToUser(response.getMemberId().toString(), "/switchRole", request.getRound() + 1);
        simpMessagingTemplate.convertAndSendToUser(response.getPairId().toString(), "/switchRole", request.getRound() + 1);
        log.info("switch code request NEXT round : {}", request.getRound() + 1);
    }

    private void sendSwitchCode(SwitchCodeRequest request, SwitchCodeResponse response){
        if (response.isHost()) {
            simpMessagingTemplate.convertAndSendToUser(response.getMemberId().toString(), "/switchCode", solveService.getPairsCode(request));
            simpMessagingTemplate.convertAndSendToUser(response.getPairId().toString(), "/switchCode", new PairCodeResponse(request.getCode(), request.getRound() + 1, request.getLanguage()));
        } else {
            simpMessagingTemplate.convertAndSendToUser(response.getPairId().toString(), "/switchCode", solveService.getPairsCode(request));
            simpMessagingTemplate.convertAndSendToUser(response.getMemberId().toString(), "/switchCode", new PairCodeResponse(request.getCode(), request.getRound() + 1, request.getLanguage()));
        }
    }


}
