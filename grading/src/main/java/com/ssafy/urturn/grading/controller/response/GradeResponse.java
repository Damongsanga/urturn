package com.ssafy.urturn.grading.controller.response;

import com.ssafy.urturn.grading.domain.Grade;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GradeResponse {
    private int languageId;
    private String stdout;
    private int statusId;
    private String stderr;
    private String token;

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
}
