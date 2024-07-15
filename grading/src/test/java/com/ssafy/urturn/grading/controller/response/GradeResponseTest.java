package com.ssafy.urturn.grading.controller.response;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.dto.response.GradeResponse;
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
        assertThat(response.stderr()).isEqualTo(null);
        assertThat(response.token()).isEqualTo("token1");
        assertThat(response.stdout()).isEqualTo("stdout1");
        assertThat(response.statusId()).isEqualTo(1);
    }

    @Test
    void EMPTY_객체를_변환할_수_있다(){
        Grade grade = Grade.EMPTY;
        GradeResponse response = GradeResponse.from(grade);
        assertThat(response.stderr()).isEqualTo("잘못된 토큰입니다.");
        assertThat(response.token()).isEqualTo(null);
        assertThat(response.stdout()).isEqualTo(null);
        assertThat(response.statusId()).isEqualTo(0);
    }
}