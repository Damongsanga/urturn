package com.ssafy.urturn.solving.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_HISTORY;

import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.service.GradingService;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.room.RoomStatus;
import com.ssafy.urturn.room.dto.RoomInfoDto;
import com.ssafy.urturn.solving.dto.ReadyInfoRequest;
import com.ssafy.urturn.solving.dto.RetroCodeResponse;
import com.ssafy.urturn.solving.dto.RetroCreateRequest;
import com.ssafy.urturn.solving.dto.SubmitRequest;
import com.ssafy.urturn.solving.dto.SubmitResponse;
import com.ssafy.urturn.solving.dto.SwitchCodeRequest;
import com.ssafy.urturn.solving.dto.SwitchCodeResponse;
import com.ssafy.urturn.solving.dto.UserCodeDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SolveService {
    private final GradingService gradingService;
    private final ProblemService problemService;
    private final CacheDatas cacheDatas;
    private final ReentrantLock lock;
    private final HistoryRepository historyRepository;

    public List<ProblemTestcaseDto> getTwoProblems(String roomId, Level level){

        RoomInfoDto roomInfoDto = cacheDatas.getRoomInfo(roomId);

        List<ProblemTestcaseDto> selectedProblems = problemService.getTwoProblem(
                roomInfoDto.getManagerId(),
                roomInfoDto.getPairId(), level);

        roomInfoDto.setProblem1Id(selectedProblems.get(0).getProblemId());
        roomInfoDto.setProblem2Id(selectedProblems.get(1).getProblemId());

        cacheDatas.putRoomInfo(roomId,roomInfoDto);

        return selectedProblems;
    }

    public void setReadyInRoomInfo(ReadyInfoRequest readyInfoRequest) {
        String roomId = readyInfoRequest.getRoomId();
        RoomInfoDto roomInfo = cacheDatas.getRoomInfo(roomId);
        boolean isHost = readyInfoRequest.isHost();

        // 준비 상태 업데이트
        updateReadyStatus(roomId, roomInfo, isHost);
        updateCodeCache(roomId, readyInfoRequest.getProblemId().toString(), null);
    }

    public boolean isReadyToSolve(String roomId){
        RoomInfoDto roomInfo = cacheDatas.getRoomInfo(roomId);
        // 두 사용자가 모두 준비되었는지 확인
        if (areBothpairsReady(roomId, roomInfo)) {
            // 두 사용자가 모두 준비완료를 했을 경우.
            roomInfo.setRoomStatus(RoomStatus.IN_GAME);
            return true;
        }
        return false;
    }

    public void updateReadyStatus(String roomId, RoomInfoDto roomInfo, boolean isHost) {
        lock.lock();
        try{
            if (isHost) {
                roomInfo.setManagerIsReady(true);

            } else {
                roomInfo.setPairIsReady(true);
            }
            cacheDatas.putRoomInfo(roomId, roomInfo);
        }finally {
            lock.unlock();
        }

    }

    public boolean areBothpairsReady(String roomId, RoomInfoDto roomInfo) {
        lock.lock();
        try {
            if (roomInfo.isManagerIsReady() && roomInfo.isPairIsReady()) {
                roomInfo.setManagerIsReady(false);
                roomInfo.setPairIsReady(false);
                cacheDatas.putRoomInfo(roomId, roomInfo);
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    public SwitchCodeResponse getPairsCode(SwitchCodeRequest switchCodeRequest){

        if(switchCodeRequest.getProblemId().equals(
            cacheDatas.getRoomInfo(switchCodeRequest.getRoomId()).getProblem1Id())){
            List<UserCodeDto> codes= cacheDatas.getCacheCodes(switchCodeRequest.getRoomId(), cacheDatas.getRoomInfo(switchCodeRequest.getRoomId()).getProblem2Id().toString());
            return new SwitchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1, codes.get(codes.size()-1).getLanguage());
        }else{
            List<UserCodeDto> codes= cacheDatas.getCacheCodes(switchCodeRequest.getRoomId(), cacheDatas.getRoomInfo(switchCodeRequest.getRoomId()).getProblem1Id().toString());
            return new SwitchCodeResponse(codes.get(codes.size()-1).getCode(), switchCodeRequest.getRound()+1, codes.get(codes.size()-1).getLanguage());
        }
    }

    // GradeService로 이동하면 좋을 것 같습니다.
    @Transactional
    public SubmitResponse submitCode(SubmitRequest submitRequest){


        // db 조회 후 채점 서버로 Code 및 문제 데이터, 테케 전송
        // 결과 수신
        GradingResponse gradingResponse = gradingService.getResult(submitRequest.getProblemId(),
                submitRequest.getCode(), submitRequest.getLanguage());

        // 오답 일 경우 실패 응답 + 관련 메시지 전송
        // 정답 일 경우 DB에 정답 코드 저장.

        if (gradingResponse.isSucceeded()){
            Long historyId = cacheDatas.getRoomInfo(submitRequest.getRoomId()).getHistoryId();

            historyRepository.findById(historyId).orElseThrow(() -> new RestApiException(NO_HISTORY))
                    .setCode(submitRequest.getProblemId(), submitRequest.getCode(), submitRequest.getLanguage());
        }

        // 페어프로그래밍 모드 전환 메시지 전송

        // 결과 반환
        return SubmitResponse.builder()
                .result(gradingResponse.isSucceeded())
                .testcaseResults(gradingResponse.getTestcaseResults())
                .build();
    }


    @Transactional
    public Map<Long, RetroCodeResponse> makeRetroCodeResponse(String roomId){

        Map<Long, RetroCodeResponse> map= new HashMap<>();
        RoomInfoDto roomInfoDto = cacheDatas.getRoomInfo(roomId);
        History history = historyRepository.findById(roomInfoDto.getHistoryId())
                .orElseThrow(()->new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND, "히스토리를 찾을 수 없습니다"));

        log.info("code 1 : {}", history.getCode1());
        log.info("code 2 : {}", history.getCode2());

        RetroCodeResponse problem1CodeResponse=new RetroCodeResponse(
            cacheDatas.getCacheCodes(roomId, roomInfoDto.getProblem1Id().toString())
                , history.getCode1(), history.getLanguage1());
        RetroCodeResponse problem2CodeResponse=new RetroCodeResponse(
            cacheDatas.getCacheCodes(roomId, roomInfoDto.getProblem2Id().toString())
                , history.getCode2(), history.getLanguage2());
        map.put(roomInfoDto.getProblem1Id(), problem1CodeResponse);
        map.put(roomInfoDto.getProblem2Id(), problem2CodeResponse);
        log.info("problem1CodeResponse : {}", problem1CodeResponse);
        log.info("problem2CodeResponse : {}", problem2CodeResponse);

        return map;
    }
    @Transactional
    public void updateHistory(String roomId, HistoryResult result, int totalRound){
        History history = historyRepository.findById(cacheDatas.getRoomInfo(roomId).getHistoryId())
                .orElseThrow(() -> new RestApiException(NO_HISTORY));
        history.finalizeUpdateHistory(result, totalRound);
        historyRepository.save(history);
    }
    @Transactional
    public void updateRetro(RetroCreateRequest req, Long historyId) {
        historyRepository.findById(historyId).orElseThrow(() -> new RestApiException(NO_HISTORY))
                .setRetro(req);
    }

    @Transactional
    public void updateCodeCache(String roomId, String questionId, UserCodeDto newCode) {
        List<UserCodeDto> currentCodes = cacheDatas.getCacheCodes(roomId, questionId);
        log.info("현재 코드 사이즈 = {}", currentCodes.size());
        if(newCode!=null){
            currentCodes.add(newCode);
        }

        cacheDatas.putCacheCodes(roomId, questionId, currentCodes);
    }

}
