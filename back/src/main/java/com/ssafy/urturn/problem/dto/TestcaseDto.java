package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestcaseDto {
    private Long id;
    private Language language;
    private String stdin;
    private String expectedOutput;
}
