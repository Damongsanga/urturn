package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.problem.Language;
import lombok.Getter;

@Getter
public class TestGetResultDto {
    private Long problemId;
    private String inputCode;
    private Language language;

}
