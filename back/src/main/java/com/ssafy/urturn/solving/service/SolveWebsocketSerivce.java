package com.ssafy.urturn.solving.service;

import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.service.HistoryService;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.room.dto.RoomInfoDto;
import com.ssafy.urturn.room.service.RoomService;
import com.ssafy.urturn.solving.dto.*;
import com.ssafy.urturn.solving.dto.request.*;
import com.ssafy.urturn.solving.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SolveWebsocketSerivce {
    private final SolveService solveService;
    private final CacheDatas cacheDatas;
    private final HistoryService historyService;
    private final ProblemService problemService;
    private final RoomService roomService;
    private final ReentrantLock lock;
    private final int ROUND_LIMIT = 20;

    public ReadContextResponse readContext(SelectLevelRequest request){
        Long managerId = roomService.getManagerIdFromCache(request.getRoomId());
        Long pairId = roomService.getPairIdFromCache(request.getRoomId());
        List<ProblemTestcaseDto> problemTestcases = solveService.getTwoProblems(request.getRoomId(), request.getLevel());

        return new ReadContextResponse(managerId, pairId, problemTestcases);
    }

    public Optional<ManagerPairIdResponse> readyToSolve(ReadyInfoRequest request){
        String roomId = request.getRoomId();
        Long pairId = roomService.getPairIdFromCache(roomId);
        Long managerId = roomService.getManagerIdFromCache(roomId);

        solveService.setReadyInRoomInfo(request);
        if (!solveService.isReadyToSolve(roomId)) {
            return Optional.empty();
        }

        historyService.createHistory(roomId);

        return Optional.of(new ManagerPairIdResponse(managerId, pairId));
    }

    public ManagerPairIdResponse submitRetro(RetroCreateRequest request){
        RoomInfoDto roomInfo = roomService.getRoomInfoDto(request.getRoomId());

        // history에 retro update
        solveService.updateRetro(request, roomInfo.getHistoryId());

        // 웹소켓 끊기 요청
        // 추가로 각 멤버를 확인해서 github repository가 null이 아니면 github access token refresh 요청
        Long managerId = roomService.getManagerIdFromCache(request.getRoomId());
        Long pairId = roomService.getPairIdFromCache(request.getRoomId());

        return new ManagerPairIdResponse(managerId, pairId);
    }

    public SwitchCodeResponse switchCode(SwitchCodeRequest req) {
        String roomId = req.getRoomId();
        Long pairId = cacheDatas.getRoomInfo(roomId).getPairId();
        Long managerId = cacheDatas.getRoomInfo(roomId).getManagerId();

        lock.lock();
        try {
            // 코드 스냅샷 저장.
            solveService.updateCodeCache(req.getRoomId(), req.getProblemId().toString(), new UserCodeDto(req));

            // 라운드가 최대라운드인 경우
            if (req.getRound() == ROUND_LIMIT) {
                // DB에 기록 저장
                solveService.updateHistory(roomId, HistoryResult.FAILURE, ROUND_LIMIT);
                return new SwitchCodeResponse(true, null, null, managerId, pairId);
            }

            // 페어프로그래밍 모드라면, 드라이버에게만 메시지 받음.
            if (req.isPair()) {

                return new SwitchCodeResponse(false, true, null, managerId, pairId);
            }

            // 릴레이 모드
            // 준비완료 업데이트
            solveService.updateReadyStatus(roomId, roomService.getRoomInfoDto(roomId), req.isHost());

            // 둘다 준비상태 확인
            if (solveService.areBothpairsReady(roomId, roomService.getRoomInfoDto(roomId))) {
                return new SwitchCodeResponse(false, false, req.isHost(), managerId, pairId);
            }
            return SwitchCodeResponse.empty();
        } finally {
            lock.unlock();
        }
    }

    public SubmitCodeResponse submitCode(SubmitRequest req) {
        log.info("채점 요청 dto = {}", req);

        RoomInfoDto roomInfoDto = roomService.getRoomInfoDto(req.getRoomId());
        Long managerId = roomInfoDto.getManagerId();
        Long pairId = roomInfoDto.getPairId();

        lockDuplicateSubmission(req, roomInfoDto);

        try {
            SubmitResponse response = solveService.submitCode(req);

            // 오답인 경우
            if (!response.isResult()) {
                return new SubmitCodeResponse(false, null, null, null, null);
            }

            // 정답인 경우.
            // 멤버당 푼 문제에 대한 기록 저장
            problemService.saveMemberProblem(managerId, pairId, req.getProblemId());

            // 페어프로그래밍 모드 인 경우 DB 저장 후 retroCode 보여주기
            if (req.isPair())
                solveService.updateHistory(req.getRoomId(), HistoryResult.SUCCESS, req.getRound());


            return new SubmitCodeResponse(true, req.isHost(), req.isPair(), managerId, pairId);

        } finally {
            releaseDuplicateSubmission(req, roomInfoDto);
        }

    }

    // 중복 요청 방지
    private void lockDuplicateSubmission(SubmitRequest submitRequest, RoomInfoDto roomInfoDto) {
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
    private void releaseDuplicateSubmission(SubmitRequest submitRequest, RoomInfoDto roomInfoDto) {
        if (submitRequest.isHost()) {
            roomInfoDto.setManagerIsSubmitting(false);
        } else {
            roomInfoDto.setPairIsSubmitting(false);
        }
        cacheDatas.putRoomInfo(submitRequest.getRoomId(), roomInfoDto);
    }

}
