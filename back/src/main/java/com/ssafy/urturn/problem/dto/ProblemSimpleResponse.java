package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.member.Level;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProblemSimpleResponse {
    private Long id;
    private String title;
    private String content;
    private Level level;
}
