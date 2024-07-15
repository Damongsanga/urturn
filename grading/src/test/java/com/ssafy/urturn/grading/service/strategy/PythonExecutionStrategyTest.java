package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import com.ssafy.urturn.grading.domain.strategy.ExecutionStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
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
    void 정답을_반환할_수_있다() {

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
                .languageId(71)
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
    void 오답을_반환할_수_있다() {

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
                .languageId(71)
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
    void 런타임_에러를_반환할_수_있다() {

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
                .languageId(71)
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
    void 중복_요청에_대해_정상적으로_동작한다(){

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
                    .languageId(71)
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
