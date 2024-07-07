package com.ssafy.urturn.grading.domain.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradeCreate {
    @NotBlank
    private String sourceCode;
    @Min(1) @Max(62)
    private int languageId;

    @NotBlank
    private String stdin;
    @NotBlank
    private String expectedOutput;

    @Builder
    public GradeCreate(String sourceCode, int languageId, String stdin, String expectedOutput) {
        this.sourceCode = sourceCode;
        this.languageId = languageId;
        this.stdin = stdin;
        this.expectedOutput = expectedOutput;
    }
}
