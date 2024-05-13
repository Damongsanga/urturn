package com.ssafy.urturn.history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.member.dto.MemberSimpleDto;
import com.ssafy.urturn.member.dto.ProblemSimpleDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryResponse {
    private Long id;
    private MemberSimpleDto pair;
    private ProblemSimpleDto problem1;
    private ProblemSimpleDto problem2;
    private String code1;
    private String code2;
    private HistoryResult result;
    private int totalRound;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


}
