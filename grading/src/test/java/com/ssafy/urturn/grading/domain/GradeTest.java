package com.ssafy.urturn.grading.domain;

import com.ssafy.urturn.grading.domain.request.GradeCreate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GradeTest {

    @Test
    void Grade를_생성할_수_있다() {
        // given
        GradeCreate gradeCreate = GradeCreate.builder()
                .stdin("stdin1")
                .expectedOutput("expectedOutput1")
                .sourceCode("sourceCode1")
                .languageId(1).build();

        // when
        Grade grade = Grade.from(gradeCreate);

        // then
        assertThat(grade.isEmpty()).isFalse();
        assertThat(grade.getStdin()).isEqualTo("stdin1");
        assertThat(grade.getExpectedOutput()).isEqualTo("expectedOutput1");
        assertThat(grade.getSourceCode()).isEqualTo("sourceCode1");
        assertThat(grade.getToken()).isNull();
        assertThat(grade.getLanguageId()).isEqualTo(1);

    }

    @Test
    void token을_삽입할_수_있다() {
        // given
        GradeCreate gradeCreate = GradeCreate.builder()
                .stdin("stdin1")
                .expectedOutput("expectedOutput1")
                .sourceCode("sourceCode1")
                .languageId(1).build();
        Grade grade = Grade.from(gradeCreate);

        // when
        grade = grade.setToken("token1");

        // then
        assertThat(grade.isEmpty()).isFalse();
        assertThat(grade.getStdin()).isEqualTo("stdin1");
        assertThat(grade.getExpectedOutput()).isEqualTo("expectedOutput1");
        assertThat(grade.getSourceCode()).isEqualTo("sourceCode1");
        assertThat(grade.getToken()).isEqualTo("token1");
        assertThat(grade.getLanguageId()).isEqualTo(1);
    }

    @Test
    void statusId를_변경할_수_있다() {
        // given
        Grade grade = Grade.builder()
                .token("token1")
                .languageId(1)
                .stdout("stdout1")
                .statusId(1)
                .build();

        // when
        grade = grade.updateStatus(GradeStatus.COMPILATION_ERROR);

        // then
        assertThat(grade.isEmpty()).isFalse();
        assertThat(grade.getStatusId()).isEqualTo(GradeStatus.COMPILATION_ERROR.getId());
        assertThat(grade.getStdout()).isEqualTo(GradeStatus.COMPILATION_ERROR.getDescription());
        assertThat(grade.getToken()).isEqualTo("token1");
    }

    @Test
    void EMPTY_객체를_만들고_EMPTY_객체인지_확인할_수_있다() {
        Grade emptyGrade = Grade.EMPTY;

        assertThat(emptyGrade.isEmpty()).isTrue();
        assertThat(emptyGrade.getStatusId()).isEqualTo(0);
        assertThat(emptyGrade.getStdin()).isNull();
        assertThat(emptyGrade.getStdout()).isNull();
    }

    @Test
    void languageId에_따라_Strategy_이름을_선정할_수_있다(){
        Grade grade = Grade.builder()
                .token("token1")
                .languageId(2)
                .stdout("stdout1")
                .statusId(1)
                .build();

        assertThat(grade.getStrategyName()).isEqualTo("pythonExecutionStrategy");
    }
}