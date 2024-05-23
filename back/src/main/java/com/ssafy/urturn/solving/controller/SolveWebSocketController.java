package com.ssafy.urturn.solving.controller;

import static com.ssafy.urturn.solving.PairProgrammingRole.DRIVER;
import static com.ssafy.urturn.solving.PairProgrammingRole.NAVIGATOR;

import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.service.HistoryService;
import com.ssafy.urturn.member.service.MemberService;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.room.dto.RoomInfoDto;
import com.ssafy.urturn.solving.dto.ReadyInfoRequest;
import com.ssafy.urturn.solving.dto.RetroCodeResponse;
import com.ssafy.urturn.solving.dto.RetroCreateRequest;
import com.ssafy.urturn.solving.dto.SelectLevelRequest;
import com.ssafy.urturn.solving.dto.SubmitRequest;
import com.ssafy.urturn.solving.dto.SubmitResponse;
import com.ssafy.urturn.solving.dto.SwitchCodeRequest;
import com.ssafy.urturn.solving.dto.SwitchCodeResponse;
import com.ssafy.urturn.solving.dto.UserCodeDto;
import com.ssafy.urturn.solving.service.SolveService;
import java.util.List;
import java.util.Map;
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
    private final CacheDatas cacheDatas;
    private final HistoryService historyService;
    private final MemberService memberService;
    private final ProblemService problemService;
    private final int ROUND_LIMIT = 20;

    @MessageMapping("/selectLevel")
    public void readContext(@Payload SelectLevelRequest selectLevelRequest) {
        log.info("난이도 선택 및 시작 로직 SelectLevel Dto = {}", selectLevelRequest);

        Long pairId = getPairIdFromCache(selectLevelRequest.getRoomId());
        Long managerId = getManagerIdFromCache(selectLevelRequest.getRoomId());

        List<ProblemTestcaseDto> problemTestcases = solveService.getTwoProblems(selectLevelRequest.getRoomId(), selectLevelRequest.getLevel());
        log.info("problemTestcases : {}", problemTestcases);
        sendTwoProblemTestcases(pairId, managerId, problemTestcases);
    }

    @MessageMapping("/readyToSolve")
    public synchronized void readyToSolve(@Payload ReadyInfoRequest readyInfoRequest) {
        log.info("문제 풀이 시작 로직 readyInfo Dto = {}", readyInfoRequest);

        String roomId = readyInfoRequest.getRoomId();
        Long pairId = getPairIdFromCache(roomId);
        Long managerId = getManagerIdFromCache(roomId);

        solveService.setReadyInRoomInfo(readyInfoRequest);
        if (!solveService.isReadyToSolve(roomId)) {
            return;
        }

        historyService.createHistory(roomId);
        sendStartSolveRequest(pairId, managerId);
    }

    @MessageMapping("/switchCode")
    public synchronized void switchCode(@Payload SwitchCodeRequest req) {
        log.info("코드 스위치 로직 SwitchCode Dto = {}", req);

        // 코드 스냅샷 저장.
        solveService.updateCodeCache(req.getRoomId(), req.getProblemId().toString(), new UserCodeDto(req));

        String roomId = req.getRoomId();
        Long pairId = cacheDatas.getRoomInfo(roomId).getPairId();
        Long managerId = cacheDatas.getRoomInfo(roomId).getManagerId();

        // 라운드가 최대라운드인 경우
        if (req.getRound() == ROUND_LIMIT) {
            // DB에 기록 저장
            solveService.updateHistory(roomId, HistoryResult.FAILURE, ROUND_LIMIT);
            // 회고창으로 전환
            sendRetroCode(roomId, pairId, managerId);
            return;
        }

        // 페어프로그래밍 모드라면, 드라이버에게만 메시지 받음.
        if (req.isPair()) {
            // 역할 전환 메시지.
            log.info("switch code request NEXT round : {}", req.getRound() + 1);
            sendSwitchRequest(req, pairId, managerId);

            return;
        }

        // 릴레이 모드
        // 준비완료 업데이트
        solveService.updateReadyStatus(roomId, getRoomInfoDto(roomId), req.isHost());

        // 둘다 준비상태 확인
        if (solveService.areBothpairsReady(roomId, getRoomInfoDto(roomId))) {
            if (req.isHost()) {
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/switchCode", solveService.getPairsCode(req));
                simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/switchCode", new SwitchCodeResponse(req.getCode(), req.getRound() + 1, req.getLanguage()));
            } else {
                simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/switchCode", solveService.getPairsCode(req));
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/switchCode", new SwitchCodeResponse(req.getCode(), req.getRound() + 1, req.getLanguage()));
            }
        }

    }

    @MessageMapping("/submitCode")
    public void submitCode(@Payload SubmitRequest req) {
        log.info("채점 요청 dto = {}", req);

        RoomInfoDto roomInfoDto = getRoomInfoDto(req.getRoomId());
        Long pairId = roomInfoDto.getPairId();
        Long managerId = roomInfoDto.getManagerId();

        duplicateSubmissionValidation(req, roomInfoDto);

        try {
            SubmitResponse response = solveService.submitCode(req);
            log.info("submitResponse : {}", response);

            // 오답인 경우
            if (!response.isResult()) {
                simpMessagingTemplate.convertAndSendToUser(req.isHost()? managerId.toString() : pairId.toString(), "/submit/result", response);
                return;
            }

            // 정답인 경우.
            // 멤버당 푼 문제에 대한 기록 저장
            problemService.saveMemberProblem(managerId, pairId, req.getProblemId());

            // 문제를 푼 사람이 호스트 인 경우.
            if (req.isHost()) {
                // 사용자에게 정답 응답 및 메시지 전송.
                simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/submit/result", response);
                // 현재 페어프로그래밍 모드가 아닌 릴레이모드라면,
                if (!req.isPair()) {
                    sendRole(managerId, pairId);
                }
            } else {
                simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/submit/result", response);
                if (!req.isPair()) {
                    sendRole(pairId, managerId);
                }
            }

            // 페어프로그래밍 모드 인 경우 DB 저장 후 retroCode 보여주기
            if (req.isPair()){
                solveService.updateHistory(req.getRoomId(), HistoryResult.SUCCESS, req.getRound());
                sendRetroCode(req.getRoomId(), pairId, managerId);
            }

        } finally {
            resetValidation(req, roomInfoDto);
        }
    }

    // 중복 요청 방지
    private void duplicateSubmissionValidation(SubmitRequest submitRequest, RoomInfoDto roomInfoDto) {
        if (submitRequest.isHost()) {
            if (roomInfoDto.isManagerIsSubmitting())
                throw new RestApiException(CustomErrorCode.REQUEST_LOCKED);
            roomInfoDto.setManagerIsSubmitting(true);
        } else {
            if (roomInfoDto.isPairIsSubmitting())
                throw new RestApiException(CustomErrorCode.REQUEST_LOCKED);
            roomInfoDto.setPairIsSubmitting(true);
        }
        cacheDatas.putRoomInfo(submitRequest.getRoomId(), roomInfoDto);
    }

    // 중복 요청 방지 원복
    private void resetValidation(SubmitRequest submitRequest, RoomInfoDto roomInfoDto) {
        if (submitRequest.isHost()) {
            roomInfoDto.setManagerIsSubmitting(false);
        } else {
            roomInfoDto.setPairIsSubmitting(false);
        }
        cacheDatas.putRoomInfo(submitRequest.getRoomId(), roomInfoDto);
    }

    @MessageMapping("/submitRetro")
    public void submitRetro(@Payload RetroCreateRequest req) {
        RoomInfoDto roomInfo = getRoomInfoDto(req.getRoomId());

        // history에 retro update
        solveService.updateRetro(req, roomInfo.getHistoryId());

        // 웹소켓 끊기 요청
        // 추가로 각 멤버를 확인해서 github repository가 null이 아니면 github access token refresh 요청
        Long managerId = getManagerIdFromCache(req.getRoomId());
        Long pairId = getPairIdFromCache(req.getRoomId());
        sendGithubUploadRequest(managerId, pairId);

        //  캐시 삭제는 현재 방에 속한 유저(방장, 페어 무관)가 다시 방을 만들거나 들어갈 때 삭제한다
        //  TTL인 하루동안은 데이터를 남겨놓도록 한다.
    }

    private void sendGithubUploadRequest(Long managerId, Long pairId) {
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/finishSocket/githubUpload", memberService.hasGithubRepository(managerId));
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/finishSocket/githubUpload", memberService.hasGithubRepository(pairId));
    }

    private void sendTwoProblemTestcases(Long pairId, Long managerId, List<ProblemTestcaseDto> problemTestcases) {
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/questionInfo", problemTestcases);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/questionInfo", problemTestcases);
    }

    private void sendStartSolveRequest(Long pairId, Long managerId) {
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/startToSolve", true);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/startToSolve", true);
    }

    private void sendRole(Long pairId, Long managerId) {
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/role", NAVIGATOR.getValue());
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/role", DRIVER.getValue());
    }

    private void sendSwitchRequest(SwitchCodeRequest req, Long pairId, Long managerId) {
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/switchRole", req.getRound() + 1);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/switchRole", req.getRound() + 1);
    }


    private void sendRetroCode(String roomId, Long pairId, Long managerId) {
        // 회고창에서 보여줄 데이터
        Map<Long, RetroCodeResponse> map = solveService.makeRetroCodeResponse(roomId);
        simpMessagingTemplate.convertAndSendToUser(managerId.toString(), "/showRetroCode", map);
        simpMessagingTemplate.convertAndSendToUser(pairId.toString(), "/showRetroCode", map);
    }

    private Long getManagerIdFromCache(String roomId) {
        return cacheDatas.getRoomInfo(roomId).getManagerId();
    }

    private Long getPairIdFromCache(String roomId) {
        return cacheDatas.getRoomInfo(roomId).getPairId();
    }

    private RoomInfoDto getRoomInfoDto(String roomId) {
        return cacheDatas.getRoomInfo(roomId);
    }
}
