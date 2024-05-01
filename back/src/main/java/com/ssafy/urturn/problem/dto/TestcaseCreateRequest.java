package com.ssafy.urturn.problem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestcaseCreateRequest {
    private Language language;
    private String stdin;
    private String expectedOutput;
    @JsonProperty("public")
    private boolean isPublic;
}
