package com.ssafy.urturn.history.repository;

import com.ssafy.urturn.history.dto.response.HistoryResponse;
import com.ssafy.urturn.history.dto.HistoryRetroDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryCustomRepository {
    Page<HistoryResponse> getHistories(Long memberId, Pageable pageable);

    HistoryRetroDto getMostRecentHistoryByMemberId(Long memberId);
}
