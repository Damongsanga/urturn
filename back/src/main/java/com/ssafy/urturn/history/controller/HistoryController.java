package com.ssafy.urturn.history.controller;

import com.ssafy.urturn.history.dto.HistoryResponse;
import com.ssafy.urturn.history.service.HistoryService;
import com.ssafy.urturn.member.dto.MemberDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("")
    public ResponseEntity<Slice<HistoryResponse>> get(@PageableDefault(size = 6) Pageable pagable){
        return ResponseEntity.ok(historyService.getHistories(pagable));
    }
}
