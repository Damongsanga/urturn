package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import com.ssafy.urturn.grading.service.strategy.ExecutionStrategy;
import com.ssafy.urturn.grading.service.strategy.JavaExecutionStrategy;
import com.ssafy.urturn.grading.service.strategy.PythonExecutionStrategy;
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
//@TestPropertySource("classpath:application-test.yml")
public class PythonExecutionStrategyTest {

    @Autowired
    ExecutionStrategy pythonExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정상_정답_테스트() {

        String sourceCode = """
                def main():
                  N = int(input())
                  soldiers = list(map(int, input().split()))
                  dp = [1] * N
                 
                  for i in range(N):
                    for j in range(i):
                      if soldiers[j] > soldiers[i]:
                        dp[i] = max(dp[i], dp[j] + 1)
                  print(N - max(dp))
                 
                if __name__ == "__main__":
                  main()""";

        String stdIn = """
                7
                15 11 4 8 5 2 4""";

        String expectedOutput = "2";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-python-accepted")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = pythonExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.ACCEPTED);
    }

    @Test
    void 오답_테스트() {

        String sourceCode = """
                def main():
                  N = int(input())
                  soldiers = list(map(int, input().split()))
                  dp = [1] * N
                 
                  for i in range(N):
                    for j in range(i):
                      if soldiers[j] > soldiers[i]:
                        dp[i] = max(dp[i], dp[j] + 1)
                  print(N - max(dp))
                 
                if __name__ == "__main__":
                  main()""";

        String stdIn = """
                7
                15 11 4 8 5 2 4""";

        String expectedOutput = "3";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-python-wrong")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = pythonExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.WRONG_ANSWER);

    }

    @Test
    void 런타임_에러_테스트() {

        String sourceCode = """
                def main():
                  N = int(input())
                  soldiers = list(map(int, input().split()))
                  dp = [1] * N
                  런타임 에러용 코드
                 
                  for i in range(N):
                    for j in range(i):
                      if soldiers[j] > soldiers[i]:
                        dp[i] = max(dp[i], dp[j] + 1)
                  print(N - max(dp))
                 
                if __name__ == "__main__":
                  main()""";

        String stdIn = """
                7
                15 11 4 8 5 2 4""";

        String expectedOutput = "2";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-python-runtime")
                .languageId(2)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);


        CompletableFuture<TokenWithStatus> future = pythonExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER);

    }

    @Test
    void 중복_요청(){

        String sourceCode = """
                def main():
                  N = int(input())
                  soldiers = list(map(int, input().split()))
                  dp = [1] * N
                 
                  for i in range(N):
                    for j in range(i):
                      if soldiers[j] > soldiers[i]:
                        dp[i] = max(dp[i], dp[j] + 1)
                  print(N - max(dp))
                 
                if __name__ == "__main__":
                  main()""";

        String stdIn = """
                7
                15 11 4 8 5 2 4""";

        String expectedOutput = "2";

        List<Grade> grades = new ArrayList<>();

        for (int idx = 0; idx < 10; idx++) {
            Grade grade = Grade.builder()
                    .token("token-aaaa-aaaa-python-" + idx)
                    .languageId(1)
                    .sourceCode(sourceCode)
                    .stdin(stdIn)
                    .expectedOutput(expectedOutput)
                    .statusId(GradeStatus.IN_QUEUE.getId())
                    .build();
            grades.add(grade);
            gradeRepository.save(grade);
        }


        grades.stream().map(pythonExecutionStrategy::execute).forEach(
                future -> assertThat(future.join().getStatus()).isEqualTo(GradeStatus.ACCEPTED)
        );

    }
}
