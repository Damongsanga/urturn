package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
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
public class CppExecutionStrategyTest {

    @Autowired
    ExecutionStrategy cppExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정답을_반환할_수_있다(){
        String sourceCode = """
                #include <iostream>
                #include <cstdlib>
                #include <cstring>
                #include <algorithm>
                                
                struct info {
                    int age;
                    char name[101];
                };
                                
                bool compare(const info& a, const info& b) {
                    // 오름차순 정렬
                    if (a.age < b.age) return true;
                    else if (a.age > b.age) return false;
                    else return false;
                }
                                
                int main() {
                    int n;
                    std::cin >> n;
                    info* p_info = new info[n];
                                
                    for (int i = 0; i < n; i++) {
                        std::cin >> p_info[i].age >> p_info[i].name;
                    }
                    std::sort(p_info, p_info + n, compare);   // 나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        std::cout << p_info[i].age << " " << p_info[i].name << std::endl;
                    }
                    delete[] p_info;
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
                .token("token-aaaa-aaaa-cpp-accepted")
                .languageId(54)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = cppExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.ACCEPTED);
    }

    @Test
    void 오답을_반환할_수_있다(){
        String sourceCode = """
                #include <iostream>
                #include <cstdlib>
                #include <cstring>
                #include <algorithm>
                                
                struct info {
                    int age;
                    char name[101];
                };
                                
                bool compare(const info& a, const info& b) {
                    // 오름차순 정렬
                    if (a.age < b.age) return true;
                    else if (a.age > b.age) return false;
                    else return false;
                }
                                
                int main() {
                    int n;
                    std::cin >> n;
                    info* p_info = new info[n];
                                
                    for (int i = 0; i < n; i++) {
                        std::cin >> p_info[i].age >> p_info[i].name;
                    }
                    std::sort(p_info, p_info + n, compare);   // 나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        std::cout << p_info[i].age << " " << p_info[i].name << std::endl;
                    }
                    delete[] p_info;
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
                .token("token-aaaa-aaaa-cpp-wrong")
                .languageId(54)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = cppExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.WRONG_ANSWER);
    }

    @Test
    void 컴파일_에러를_반환할_수_있다(){

        String sourceCode = """
                #include <iostream>
                #include <cstdlib>
                #include <cstring>
                #include <algorithm>
                
                컴파일 에러
                                
                struct info {
                    int age;
                    char name[101];
                };
                                
                bool compare(const info& a, const info& b) {
                    // 오름차순 정렬
                    if (a.age < b.age) return true;
                    else if (a.age > b.age) return false;
                    else return false;
                }
                                
                int main() {
                    int n;
                    std::cin >> n;
                    info* p_info = new info[n];
                                
                    for (int i = 0; i < n; i++) {
                        std::cin >> p_info[i].age >> p_info[i].name;
                    }
                    std::sort(p_info, p_info + n, compare);   // 나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        std::cout << p_info[i].age << " " << p_info[i].name << std::endl;
                    }
                    delete[] p_info;
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
                .token("token-aaaa-aaaa-cpp-complie")
                .languageId(54)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = cppExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.COMPILATION_ERROR);
    }

    @Test
    void 런타임_에러를_반환할_수_있다(){
        String sourceCode = """
                #include <iostream>
                #include <cstdlib>
                #include <cstring>
                #include <algorithm>
                               
                struct info {
                    int age;
                    char name[101];
                };
                               
                bool compare(const info& a, const info& b) {
                    // 오름차순 정렬
                    if (a.age < b.age) return true;
                    else if (a.age > b.age) return false;
                    else return false;
                }
                               
                int main() {
                    int *ptr = NULL;
                    *ptr = 10; // 런타임 에러 발생 (Segmentation Fault)
                    
                    int n;
                    std::cin >> n;
                    info* p_info = new info[n];
                               
                    for (int i = 0; i < n; i++) {
                        std::cin >> p_info[i].age >> p_info[i].name;
                    }
                    std::sort(p_info, p_info + n, compare);   // 나이 순 정렬
                               
                    for (int i = 0; i < n; i++) {
                        std::cout << p_info[i].age << " " << p_info[i].name << std::endl;
                    }
                    delete[] p_info;
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
                .token("token-aaaa-aaaa-cpp-runtime")
                .languageId(54)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = cppExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER);
    }

    @Test
    void 중복_요청에_대해_정상적으로_동작한다() {

        String sourceCode = """
                #include <iostream>
                #include <cstdlib>
                #include <cstring>
                #include <algorithm>
                                
                struct info {
                    int age;
                    char name[101];
                };
                                
                bool compare(const info& a, const info& b) {
                    // 오름차순 정렬
                    if (a.age < b.age) return true;
                    else if (a.age > b.age) return false;
                    else return false;
                }
                                
                int main() {
                    int n;
                    std::cin >> n;
                    info* p_info = new info[n];
                                
                    for (int i = 0; i < n; i++) {
                        std::cin >> p_info[i].age >> p_info[i].name;
                    }
                    std::sort(p_info, p_info + n, compare);   // 나이 순 정렬
                                
                    for (int i = 0; i < n; i++) {
                        std::cout << p_info[i].age << " " << p_info[i].name << std::endl;
                    }
                    delete[] p_info;
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
                    .token("token-aaaa-aaaa-cpp-" + idx)
                    .languageId(54)
                    .sourceCode(sourceCode)
                    .stdin(stdIn)
                    .expectedOutput(expectedOutput)
                    .statusId(GradeStatus.IN_QUEUE.getId())
                    .build();
            grades.add(grade);
            gradeRepository.save(grade);
        }

        grades.stream().map(cppExecutionStrategy::execute).forEach(
                future -> assertThat(future.join().getStatus()).isEqualTo(GradeStatus.ACCEPTED)
        );

    }

}
