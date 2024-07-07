package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import com.ssafy.urturn.grading.service.strategy.ExecutionStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class JavascriptExecutionStrategyTest {

    @Autowired
    ExecutionStrategy javascriptExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정상_정답_테스트(){
        String sourceCode = """
                const readline = require('readline').createInterface({
                    input: process.stdin,
                    output: process.stdout,
                });
                
                let input = [];
                
                readline.on('line', function(line) {
                    input = line.split(' ').map(el => parseInt(el));
                }).on('close', function(){ //이 안에 솔루션 코드 작성
                    const A = parseInt(input[0]);
                    const B = parseInt(input[1]);
                    console.log(A+B);
                    process.exit();
                });""";

        String stdIn = """
                1 2""";

        String expectedOutput = "3";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-javascript-accepted")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javascriptExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.ACCEPTED);
    }

    @Test
    void 오답_테스트() {
        // when
        String sourceCode = """
                const readline = require('readline').createInterface({
                    input: process.stdin,
                    output: process.stdout,
                });
                
                let input = [];
                
                readline.on('line', function(line) {
                    input = line.split(' ').map(el => parseInt(el));
                }).on('close', function(){ //이 안에 솔루션 코드 작성
                    const A = parseInt(input[0]);
                    const B = parseInt(input[1]);
                    console.log(A+B);
                    process.exit();
                });""";

        String stdIn = """
                1 2""";

        String expectedOutput = "4";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-javascript-wrong")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        // given
        CompletableFuture<TokenWithStatus> future = javascriptExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();

        //then
        assertThat(res.getStatus()).isEqualTo(GradeStatus.WRONG_ANSWER);
    }

    @Test
    void 런타임_에러_테스트(){
        String sourceCode = """
                const readline = require('readline').createInterface({
                    input: process.stdin,
                    output: process.stdout,
                });
                
                런타임 에러 코드
                
                let input = [];
                
                readline.on('line', function(line) {
                    input = line.split(' ').map(el => parseInt(el));
                }).on('close', function(){ //이 안에 솔루션 코드 작성
                    const A = parseInt(input[0]);
                    const B = parseInt(input[1]);
                    console.log(A+B);
                    process.exit();
                });""";

        String stdIn = """
                1 2""";

        String expectedOutput = "3";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-javascript-runtime")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javascriptExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER);
    }

    @Test
    void 중복_요청(){
        String sourceCode = """
                const readline = require('readline').createInterface({
                    input: process.stdin,
                    output: process.stdout,
                });
                
                let input = [];
                
                readline.on('line', function(line) {
                    input = line.split(' ').map(el => parseInt(el));
                }).on('close', function(){ //이 안에 솔루션 코드 작성
                    const A = parseInt(input[0]);
                    const B = parseInt(input[1]);
                    console.log(A+B);
                    process.exit();
                });""";

        String stdIn = """
                1 2""";

        String expectedOutput = "3";

        List<Grade> grades = new ArrayList<>();

        for (int idx = 0; idx < 10; idx++) {
            Grade grade = Grade.builder()
                    .token("token-aaaa-aaaa-javascript-" + idx)
                    .languageId(1)
                    .sourceCode(sourceCode)
                    .stdin(stdIn)
                    .expectedOutput(expectedOutput)
                    .statusId(GradeStatus.IN_QUEUE.getId())
                    .build();
            grades.add(grade);
            gradeRepository.save(grade);
        }

        grades.stream().map(javascriptExecutionStrategy::execute).forEach(
                future -> assertThat(future.join().getStatus()).isEqualTo(GradeStatus.ACCEPTED)
        );

    }
}
