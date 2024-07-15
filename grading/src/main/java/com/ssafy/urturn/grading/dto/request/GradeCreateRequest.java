package com.ssafy.urturn.grading.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record GradeCreateRequest(
    @NotEmpty
    String sourceCode,
    @Min(1) @Max(74)
    int languageId,
    @NotEmpty
    String stdin,
    @NotEmpty
    String expectedOutput
){
}
