package com.ssafy.urturn.grading.controller.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.urturn.grading.domain.Grade;
import lombok.*;
import org.apache.tomcat.util.buf.Utf8Encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GradeResponse {
    private int languageId;
    private String stdout;
    private int statusId;
    private String stderr;
    private String token;

    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static GradeResponse from(Grade grade){
        if (grade.getToken() == null){
            return GradeResponse.builder()
                    .stderr("잘못된 토큰입니다.")
                    .build();
        }

        return GradeResponse.builder()
                .languageId(grade.getLanguageId())
                .stdout(grade.getStdout())
                .statusId(grade.getStatusId())
                .stderr(grade.getStderr())
                .token(grade.getToken())
                .build();
    }

    public static GradeResponse fromBase64Encoded(Grade grade) {
        if (grade.getToken() == null) {
            return GradeResponse.builder()
                    .stderr(encodeNullable("잘못된 토큰입니다."))
                    .build();
        }

        return GradeResponse.builder()
                .languageId(grade.getLanguageId())
                .stdout(encodeNullable(grade.getStdout()))
                .statusId(grade.getStatusId())
                .stderr(encodeNullable(grade.getStderr()))
                .token(encoder.encodeToString(grade.getToken().getBytes()))
                .build();
    }

    private static String encodeNullable(String data) {
        return data == null ? null : encoder.encodeToString(data.getBytes());
    }
}
