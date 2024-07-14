package com.ssafy.urturn.grading.domain;

import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void grade를_저장_및_조회할_수_있다(){
        // given
        Grade grade = Grade.builder()
                .token("token1")
                .languageId(1)
                .stdout("stdout1")
                .statusId(1)
                .build();

        // when
        gradeRepository.save(grade);
        Optional<Grade> findGrade = gradeRepository.findByToken("token1");

        // then
        assertThat(findGrade).isNotEmpty();
        assertThat(findGrade.get().getToken()).isEqualTo("token1");
        assertThat(findGrade.get().getLanguageId()).isEqualTo(1);
        assertThat(findGrade.get().getStatusId()).isEqualTo(1);
        assertThat(findGrade.get().getStdout()).isEqualTo("stdout1");
    }
}