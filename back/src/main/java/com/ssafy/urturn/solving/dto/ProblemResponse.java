package com.ssafy.urturn.solving.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResponse {
    private Long problemId;
    private String problemUrl;
    private String problemTitle;
}
