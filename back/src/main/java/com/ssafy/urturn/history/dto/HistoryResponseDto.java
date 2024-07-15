package com.ssafy.urturn.history.dto;

import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.dto.response.HistoryResponse;
import com.ssafy.urturn.member.dto.MemberSimpleDto;
import com.ssafy.urturn.member.dto.ProblemSimpleDto;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryResponseDto {
    private Long id;
    private MemberSimpleDto manager;
    private MemberSimpleDto pair;
    private ProblemSimpleDto problem1;
    private ProblemSimpleDto problem2;
    private String code1;
    private String code2;
    private HistoryResult result;
    private int totalRound;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public HistoryResponse toResponse(Long memberId){

        MemberSimpleDto memberDto = Objects.equals(manager.getId(), memberId) ? this.pair : this.manager;

        return HistoryResponse.builder()
            .id(this.id)
            .pair(memberDto)
            .problem1(this.problem1)
            .problem2(this.problem2)
            .code1(this.code1)
            .code2(this.code2)
            .result(this.result)
            .totalRound(this.totalRound)
            .startTime(this.startTime)
            .endTime(this.endTime)
            .build();
    }


}
