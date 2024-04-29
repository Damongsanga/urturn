package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.member.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProblemCreateRequest {
    private String title;
    private String content;
    private Level level;
}
