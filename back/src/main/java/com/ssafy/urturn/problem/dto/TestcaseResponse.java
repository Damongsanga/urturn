package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestcaseResponse {
    private Long id;
    private Language language;
    private String stdin;
    private String expectedOutput;
    private boolean isPublic;
}
