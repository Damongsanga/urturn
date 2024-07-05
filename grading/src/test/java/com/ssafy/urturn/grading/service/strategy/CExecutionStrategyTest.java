package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.strategy.ExecutionStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
public class CExecutionStrategyTest {

    @Autowired
    ExecutionStrategy CExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @Test
    void 정상_정답_테스트(){
//        String sourceCode = """
//                #include <stdio.h>
//                #include <stdlib.h>
//
//                struct info{	//정보가 있는 구조체
//                	int age;
//                	char name[101];
//                };
//
//                int compare(const void* arg1, const void* arg2) {	//정렬
//                	info *a = (info*)arg1;    //void를 int형으로 변환
//                	info *b = (info*)arg2;
//                	//오름차순 정렬
//                	if (a->age > b->age) return 1;
//                	else if (a->age < b->age) return -1;
//                	else return 0;
//                }
//
//                int main(void)
//                {
//                	int n;
//                	scanf("%d", &n);
//                	info* p_info = (info*)malloc(sizeof(info) * n);
//
//                	for (int i = 0; i < n; i++) {	//입력
//                		scanf("%d%*c%s", &p_info[i].age, p_info[i].name);
//                	}
//                	qsort(p_info, n, sizeof(info), compare);	//나이 순 정렬
//
//                	for (int i = 0; i < n; i++) {	//출력
//                		printf("%d %s\\n", p_info[i].age, p_info[i].name);
//                	}
//                	free(p_info);	//할당 해제
//                	return 0;
//                }""";
//
//        String stdIn = """
//                3
//                21 Junkyu
//                21 Dohyun
//                20 Sunyoung""";
//
//        String expectedOutput = """
//                20 Sunyoung
//                21 Junkyu
//                21 Dohyun""";
//
//        Grade grade = Grade.builder()
//                .token("token-aaaa-aaaa-dddd")
//                .languageId(2)
//                .sourceCode(sourceCode)
//                .stdin(stdIn)
//                .expectedOutput(expectedOutput)
//                .statusId(GradeStatus.IN_QUEUE.getId())
//                .build();
//        gradeRepository.save(grade);
//
//        CExecutionStrategy.execute(grade);
//
//        Grade resultGrade = gradeRepository.findByToken("token-aaaa-aaaa-dddd").get();
//        gradeRepository.deleteByToken("token-aaaa-aaaa-dddd");
//
//        assertThat(resultGrade.getStatusId()).isEqualTo(GradeStatus.ACCEPTED.getId());
    }
}
