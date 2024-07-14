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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CExecutionStrategyTest {

    @Autowired
    ExecutionStrategy CExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정답을_반환할_수_있다(){
        String sourceCode = """
                #include <stdio.h>
                #include <stdlib.h>
                                
                typedef struct {
                    int age;
                    char name[101];
                } info;
                                
                int compare(const void* arg1, const void* arg2) {
                    info *a = (info*)arg1;
                    info *b = (info*)arg2;
                    if (a->age > b->age) return 1;
                    else if (a->age < b->age) return -1;
                    else return 0;
                }
                                
                int main(void)
                {
                    int n;
                    scanf("%d", &n);
                    info* p_info = (info*)malloc(sizeof(info) * n);
                                
                    for (int i = 0; i < n; i++) {
                        scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
                    }
                    qsort(p_info, n, sizeof(info), compare);   //나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        printf("%d %s\\n", p_info[i].age, p_info[i].name);
                    }
                    free(p_info);
                    return 0;
                }
                """;

        String stdIn = """
                3
                21 Junkyu
                21 Dohyun
                20 Sunyoung""";

        String expectedOutput = """
                20 Sunyoung
                21 Junkyu
                21 Dohyun""";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-c-accepted")
                .languageId(50)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = CExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.ACCEPTED);
    }

    @Test
    void 오답을_반환할_수_있다(){
        String sourceCode = """
                #include <stdio.h>
                #include <stdlib.h>
                                
                typedef struct {
                    int age;
                    char name[101];
                } info;
                                
                int compare(const void* arg1, const void* arg2) {
                    info *a = (info*)arg1;
                    info *b = (info*)arg2;
                    if (a->age > b->age) return 1;
                    else if (a->age < b->age) return -1;
                    else return 0;
                }
                                
                int main(void)
                {
                    int n;
                    scanf("%d", &n);
                    info* p_info = (info*)malloc(sizeof(info) * n);
                                
                    for (int i = 0; i < n; i++) {
                        scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
                    }
                    qsort(p_info, n, sizeof(info), compare);   //나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        printf("%d %s\\n", p_info[i].age, p_info[i].name);
                    }
                    free(p_info);
                    return 0;
                }
                """;

        String stdIn = """
                3
                21 Junkyu
                21 Dohyun
                20 Sunyoung""";

        String expectedOutput = """
                21 Junkyu
                21 Dohyun""";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-c-wrong")
                .languageId(50)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = CExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.WRONG_ANSWER);
    }

    @Test
    void 컴파일_에러를_반환할_수_있다(){
        String sourceCode = """
                #include <stdio.h>
                #include <stdlib.h>
                                
                컴파일 에러 
                typedef struct {
                    int age;
                    char name[101];
                } info;
                                
                int compare(const void* arg1, const void* arg2) {
                    info *a = (info*)arg1;
                    info *b = (info*)arg2;
                    if (a->age > b->age) return 1;
                    else if (a->age < b->age) return -1;
                    else return 0;
                }
                                
                int main(void)
                {
                    int n;
                    scanf("%d", &n);
                    info* p_info = (info*)malloc(sizeof(info) * n);
                                
                    for (int i = 0; i < n; i++) {
                        scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
                    }
                    qsort(p_info, n, sizeof(info), compare);   //나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        printf("%d %s\\n", p_info[i].age, p_info[i].name);
                    }
                    free(p_info);
                    return 0;
                }
                """;

        String stdIn = """
                3
                21 Junkyu
                21 Dohyun
                20 Sunyoung""";

        String expectedOutput = """
                20 Sunyoung
                21 Junkyu
                21 Dohyun""";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-c-complie")
                .languageId(50)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = CExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.COMPILATION_ERROR);
    }

    @Test
    void 런타임_에러를_반환할_수_있다(){
        String sourceCode = """
                #include <stdio.h>
                #include <stdlib.h>
                                
                typedef struct {
                    int age;
                    char name[1];
                } info;
                                
                int compare(const void* arg1, const void* arg2) {
                    info *a = (info*)arg1;
                    info *b = (info*)arg2;
                    if (a->age > b->age) return 1;
                    else if (a->age < b->age) return -1;
                    else return 0;
                }
                                
                int main(void)
                {
                    int *ptr = NULL;
                    *ptr = 10; // 런타임 에러 발생 (Segmentation Fault)
                    
                    int n;
                    scanf("%d", &n);
                    info* p_info = (info*)malloc(sizeof(info) * n);
                                
                    for (int i = 0; i < n; i++) { 
                        scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
                    }
                    qsort(p_info, n, sizeof(info), compare);   //나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        printf("%d %s\\n", p_info[i].age, p_info[i].name);
                    }
                    free(p_info);
                    return 0;
                }
                """;

        String stdIn = """
                3
                20 Junkyu
                21 Dohyun
                20 Sunyoung""";

        String expectedOutput = """
                20 Sunyoung
                21 Junkyu
                21 Dohyun""";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-c-runtime")
                .languageId(50)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = CExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER);
    }

    @Test
    void 중복_요청에_대해_정상적으로_동작한다() {

        String sourceCode = """
                #include <stdio.h>
                #include <stdlib.h>
                                
                typedef struct {
                    int age;
                    char name[101];
                } info;
                                
                int compare(const void* arg1, const void* arg2) {
                    info *a = (info*)arg1;
                    info *b = (info*)arg2;
                    if (a->age > b->age) return 1;
                    else if (a->age < b->age) return -1;
                    else return 0;
                }
                                
                int main(void)
                {
                    int n;
                    scanf("%d", &n);
                    info* p_info = (info*)malloc(sizeof(info) * n);
                                
                    for (int i = 0; i < n; i++) { 
                        scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
                    }
                    qsort(p_info, n, sizeof(info), compare);   //나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        printf("%d %s\\n", p_info[i].age, p_info[i].name);
                    }
                    free(p_info);
                    return 0;
                }
                """;

        String stdIn = """
                3
                21 Junkyu
                21 Dohyun
                20 Sunyoung""";

        String expectedOutput = """
                20 Sunyoung
                21 Junkyu
                21 Dohyun""";


        List<Grade> grades = new ArrayList<>();

        for (int idx = 0; idx < 10; idx++) {
            Grade grade = Grade.builder()
                    .token("token-aaaa-aaaa-c-" + idx)
                    .languageId(50)
                    .sourceCode(sourceCode)
                    .stdin(stdIn)
                    .expectedOutput(expectedOutput)
                    .statusId(GradeStatus.IN_QUEUE.getId())
                    .build();
            grades.add(grade);
            gradeRepository.save(grade);
        }

        grades.stream().map(CExecutionStrategy::execute).forEach(
                future -> assertThat(future.join().getStatus()).isEqualTo(GradeStatus.ACCEPTED)
        );

    }

}
