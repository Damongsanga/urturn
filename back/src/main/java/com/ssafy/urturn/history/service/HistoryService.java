package com.ssafy.urturn.history.service;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.history.dto.HistoryResponse;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.repository.MemberRepository;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.room.dto.RoomInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final CacheDatas cacheDatas;

    public Page<HistoryResponse> getHistories(Pageable pageable) {
        Long currentMemberId = MemberUtil.getMemberId();
        return historyRepository.getHistories(currentMemberId, pageable);
    }

    // RoomInfoDto에 historyId 저장
    @Transactional
    public Long createHistory(String roomId){
        RoomInfoDto roomInfoDto = cacheDatas.cacheroomInfoDto(roomId);

        History history = History.builder()
            .manager(memberRepository.findById(roomInfoDto.getManagerId()).orElseThrow(() -> new RestApiException(
                CustomErrorCode.NO_MEMBER)))
            .pair(memberRepository.findById(roomInfoDto.getPairId()).orElseThrow(() -> new RestApiException(
                CustomErrorCode.NO_MEMBER)))
            .problem1(problemRepository.findById(roomInfoDto.getProblem1Id()).orElseThrow(() -> new RestApiException(CustomErrorCode.NO_PROBLEM)))
            .problem2(problemRepository.findById(roomInfoDto.getProblem2Id()).orElseThrow(() -> new RestApiException(CustomErrorCode.NO_PROBLEM)))
            .build();
        Long id = historyRepository.save(history).getId();

        roomInfoDto.setHistoryId(id);
        cacheDatas.cacheroomInfoDto(roomId, roomInfoDto);

        return id;
    }



}
