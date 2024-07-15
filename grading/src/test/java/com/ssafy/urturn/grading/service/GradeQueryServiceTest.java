package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.mock.FakeGradeRepository;
import com.ssafy.urturn.grading.mock.TestTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class GradeQueryServiceTest {

    private GradeQueryService gradeQueryService;


    @BeforeEach
    void init(){
        TestTokenManager testTokenManager = new TestTokenManager();
        FakeGradeRepository fakeGradeRepository = new FakeGradeRepository();
        this.gradeQueryService = new GradeQueryService(fakeGradeRepository, testTokenManager);

        Grade grade1 = Grade.builder()
                .token("token1")
                .languageId(1)
                .stdout("stdout1")
                .statusId(1)
                .build();

        Grade grade2 = Grade.builder()
                .token("token2")
                .languageId(2)
                .stdout("stdout2")
                .statusId(2)
                .build();

        Grade grade3 = Grade.builder()
                .token("token3")
                .languageId(3)
                .stdout("stdout3")
                .statusId(3)
                .build();

        fakeGradeRepository.save(grade1);
        fakeGradeRepository.save(grade2);
        fakeGradeRepository.save(grade3);

    }

   @Test
   void getGrades는_존재하는_결과값을_반환할_수_있다(){
       // given
       String request = "token1,token2,token3";
       // when
       // then

       List<Grade> result = gradeQueryService.getGrades(request);
       assertThat(result.size()).isEqualTo(3);
       assertThat(result.get(0).getStdout()).isEqualTo("stdout1");
       assertThat(result.get(1).getToken()).isEqualTo("token2");
       assertThat(result.get(2).getLanguageId()).isEqualTo(3);
       assertThat(result.get(0).getStdin()).isEqualTo(null);
   }

    @Test
    void getGrades는_존재하지_결과값을_Empty_객체로_반환할_수_있다(){
        // given
        String request = "token4,token5,token6";
        // when
        // then
        List<Grade> result = gradeQueryService.getGrades(request);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(2).getStdout()).isEqualTo(null);
        assertThat(result.get(2).getToken()).isEqualTo(null);
        assertThat(result.get(2).getStdin()).isEqualTo(null);
        assertThat(result.get(2).isEmpty()).isTrue();
    }

}