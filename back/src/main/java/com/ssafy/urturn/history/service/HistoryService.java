package com.ssafy.urturn.history.service;

import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.history.dto.HistoryResponse;
import com.ssafy.urturn.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public Page<HistoryResponse> getHistories(Pageable pageable) {
        Long currentMemberId = MemberUtil.getMemberId();
        return historyRepository.getHistories(currentMemberId, pageable);
    }
}
