package com.ssafy.urturn.problem.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenCreateDto {

    private String sourceCode;
    private int languageId;
    private String stdin;
    private String expectedOutput;

    public TokenCreateDto(TestcaseDto testcase, String sourceCode) {
        this.sourceCode = sourceCode;
        this.languageId = testcase.getLanguage().getLangId();
        this.stdin = testcase.getStdin();
        this.expectedOutput = testcase.getExpectedOutput();
    }
}
