package com.ssafy.urturn.grading.controller.response;

import com.ssafy.urturn.grading.domain.Grade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GradeResponseTest {

    @Test
    void Grade_응답을_할_수_있다(){
        Grade grade = Grade.builder()
                .token("token1")
                .languageId(1)
                .stdout("stdout1")
                .statusId(1)
                .build();
        GradeResponse response = GradeResponse.from(grade);
        assertThat(response.getStderr()).isEqualTo(null);
        assertThat(response.getToken()).isEqualTo("token1");
        assertThat(response.getStdout()).isEqualTo("stdout1");
        assertThat(response.getStatusId()).isEqualTo(1);
    }

    @Test
    void EMPTY_객체를_변환할_수_있다(){
        Grade grade = Grade.EMPTY;
        GradeResponse response = GradeResponse.from(grade);
        assertThat(response.getStderr()).isEqualTo("잘못된 토큰입니다.");
        assertThat(response.getToken()).isEqualTo(null);
        assertThat(response.getStdout()).isEqualTo(null);
        assertThat(response.getStatusId()).isEqualTo(0);
    }
}