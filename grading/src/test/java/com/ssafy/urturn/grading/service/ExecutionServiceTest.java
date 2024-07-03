package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.GradeStatus;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
class ExecutionServiceTest {

    @Autowired
    ExecutionService executionService;
    @Autowired
    GradeRepository gradeRepository;

    @Test
    void 정상_정답_테스트(){

        String sourceCode = """
                import java.io.*;
                import java.util.*;

                // 유기농 양상추
                // 이문제는 그래프 탐색이면 다 풀 수 있는데, dfs로 풀겠습니다.

                public class Main {

                    static int[][] field; // 배추밭 배열
                    static boolean[][] visited; // 2차원 방문 배열
                    static int M, N; // 배추밭의 가로세로 길이
                    static int[] dx = {-1, 1, 0, 0}; // 이동 배열
                    static int[] dy = {0, 0, -1, 1};\s

                    public static void main(String[] args) throws IOException {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        int T = Integer.parseInt(br.readLine()); // 테스트케이스 수

                \t\t\t\t// 테스트 케이스 FOR 문
                        for (int t = 0; t < T; t++) {
                            StringTokenizer st = new StringTokenizer(br.readLine());
                            M = Integer.parseInt(st.nextToken());
                            N = Integer.parseInt(st.nextToken());
                            int K = Integer.parseInt(st.nextToken()); // 배추의 위치의 개수

                            field = new int[N][M];
                            visited = new boolean[N][M];

                            // 배추의 위치를 입력 받아 field 배열에 표시
                            for (int i = 0; i < K; i++) {
                                st = new StringTokenizer(br.readLine());
                                int x = Integer.parseInt(st.nextToken());
                                int y = Integer.parseInt(st.nextToken());
                                field[y][x] = 1;
                            }

                            int count = 0; // 연결된 배추 그룹의 수를 저장할 변수
                            for (int i = 0; i < N; i++) {
                                for (int j = 0; j < M; j++) {
                                    // 배추가 있고, 아직 방문하지 않은 위치라면 DFS 시작
                                    if (field[i][j] == 1 && !visited[i][j]) {
                                        dfs(i, j); // 탐색
                                        count++; // DFS가 끝나면 연결된 그룹을 하나 찾은 것이므로 count 증가
                                    }
                                }
                            }
                            System.out.println(count); // 연결된 배추 그룹의 수 출력
                        }
                    }

                    static void dfs(int x, int y) {
                        visited[x][y] = true; // 방문 표시

                        // 상하좌우 방향으로의 이동
                        for (int i = 0; i < 4; i++) {
                            int nx = x + dx[i];
                            int ny = y + dy[i];

                            // nx와 ny가 배추밭의 범위 내에 있고
                            if (nx >= 0 && nx < N && ny >= 0 && ny < M) {
                                // 해당 위치에 배추가 있으며 아직 방문하지 않았다면 DFS 재귀 호출
                                if (field[nx][ny] == 1 && !visited[nx][ny]) {
                                    dfs(nx, ny);
                                }
                            }
                        }
                    }
                }""";

        String stdIn = """
                1
                5 3 6
                0 2
                1 2
                2 2
                3 2
                4 2
                4 0""";

        String expectedOutput = "2";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-aaaa")
                .languageId(1)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        executionService.execute(grade);
        Grade resultGrade = gradeRepository.findByToken("token-aaaa-aaaa-aaaa").get();

        assertThat(resultGrade.getStatusId()).isEqualTo(GradeStatus.ACCEPTED.getId());

    }

    @Test
    void 컴파일_에러_테스트(){

        String sourceCode = """
                import java.io.*;
                import java.util.*;

                // 유기농 양상추
                // 이문제는 그래프 탐색이면 다 풀 수 있는데, dfs로 풀겠습니다.

                public class Main 

                    static int[][] field; // 배추밭 배열
                    static boolean[][] visited; // 2차원 방문 배열
                    static int M, N; // 배추밭의 가로세로 길이
                    static int[] dx = {-1, 1, 0, 0}; // 이동 배열
                    static int[] dy = {0, 0, -1, 1};\s

                    public static void main(String[] args) throws IOException {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        int T = Integer.parseInt(br.readLine()); // 테스트케이스 수

                \t\t\t\t// 테스트 케이스 FOR 문
                        for (int t = 0; t < T; t++) {
                            StringTokenizer st = new StringTokenizer(br.readLine());
                            M = Integer.parseInt(st.nextToken());
                            N = Integer.parseInt(st.nextToken());
                            int K = Integer.parseInt(st.nextToken()); // 배추의 위치의 개수

                            field = new int[N][M];
                            visited = new boolean[N][M];

                            // 배추의 위치를 입력 받아 field 배열에 표시
                            for (int i = 0; i < K; i++) {
                                st = new StringTokenizer(br.readLine());
                                int x = Integer.parseInt(st.nextToken());
                                int y = Integer.parseInt(st.nextToken());
                                field[y][x] = 1;
                            }

                            int count = 0; // 연결된 배추 그룹의 수를 저장할 변수
                            for (int i = 0; i < N; i++) {
                                for (int j = 0; j < M; j++) {
                                    // 배추가 있고, 아직 방문하지 않은 위치라면 DFS 시작
                                    if (field[i][j] == 1 && !visited[i][j]) {
                                        dfs(i, j); // 탐색
                                        count++; // DFS가 끝나면 연결된 그룹을 하나 찾은 것이므로 count 증가
                                    }
                                }
                            }
                            System.out.println(count); // 연결된 배추 그룹의 수 출력
                        }
                    }

                    static void dfs(int x, int y) {
                        visited[x][y] = true; // 방문 표시

                        // 상하좌우 방향으로의 이동
                        for (int i = 0; i < 4; i++) {
                            int nx = x + dx[i];
                            int ny = y + dy[i];

                            // nx와 ny가 배추밭의 범위 내에 있고
                            if (nx >= 0 && nx < N && ny >= 0 && ny < M) {
                                // 해당 위치에 배추가 있으며 아직 방문하지 않았다면 DFS 재귀 호출
                                if (field[nx][ny] == 1 && !visited[nx][ny]) {
                                    dfs(nx, ny);
                                }
                            }
                        }
                    }
                }""";

        String stdIn = """
                1
                5 3 6
                0 2
                1 2
                2 2
                3 2
                4 2
                4 0""";

        String expectedOutput = "2";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-aaaa")
                .languageId(1)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        executionService.execute(grade);
        Grade resultGrade = gradeRepository.findByToken("token-aaaa-aaaa-aaaa").get();

        assertThat(resultGrade.getStatusId()).isEqualTo(GradeStatus.COMPILATION_ERROR.getId());

    }

    @Test
    void 런타임_에러_테스트(){

        String sourceCode = """
                import java.io.*;
                import java.util.*;

                // 유기농 양상추
                // 이문제는 그래프 탐색이면 다 풀 수 있는데, dfs로 풀겠습니다.

                public class Main{

                    static int[][] field; // 배추밭 배열
                    static boolean[][] visited; // 2차원 방문 배열
                    static int M, N; // 배추밭의 가로세로 길이
                    static int[] dx = {-1, 1, 0, 0}; // 이동 배열
                    static int[] dy = {0, 0, -1, 1};\s

                    public static void main(String[] args) throws IOException {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        int T = Integer.parseInt(br.readLine()); // 테스트케이스 수

                \t\t\t\t// 테스트 케이스 FOR 문
                        for (int t = 0; t < T; t++) {
                            StringTokenizer st = new StringTokenizer(br.readLine());
                            M = Integer.parseInt(st.nextToken());
                            N = Integer.parseInt(st.nextToken());
                            int K = Integer.parseInt(st.nextToken()); // 배추의 위치의 개수

                            field = new int[N][M];
                            visited = new boolean[N][M];

                            // 배추의 위치를 입력 받아 field 배열에 표시
                            for (int i = 0; i < K; i++) {
                                st = new StringTokenizer(br.readLine());
                                int x = Integer.parseInt(st.nextToken());
                                int y = Integer.parseInt(st.nextToken());
                                field[y][x] = 1;
                            }

                            int count = 0; // 연결된 배추 그룹의 수를 저장할 변수
                            for (int i = 0; i < N; i++) {
                                for (int j = 0; j < M; j++) {
                                    // 배추가 있고, 아직 방문하지 않은 위치라면 DFS 시작
                                    if (field[i][j] == 1 && !visited[i][j]) {
                                        dfs(i, j); // 탐색
                                        count++; // DFS가 끝나면 연결된 그룹을 하나 찾은 것이므로 count 증가
                                    }
                                }
                            }
                            System.out.println(count); // 연결된 배추 그룹의 수 출력
                        }
                    }

                    static void dfs(int x, int y) {
                        visited[x][y] = true; // 방문 표시

                        // 상하좌우 방향으로의 이동
                        for (int i = 0; i < 4; i++) {
                            int nx = x + dx[i];
                            int ny = y + dy[i];

                            // nx와 ny가 배추밭의 범위 내에 있고
                            if (nx >= 0 && nx < N && ny >= 0 && ny < M) {
                                // 해당 위치에 배추가 있으며 아직 방문하지 않았다면 DFS 재귀 호출
                                if (field[nx][ny] == 1 && !visited[nx][ny]) {
                                    dfs(nx, ny);
                                }
                            }
                        }
                    }
                }""";

        String stdIn = """
                1 2
                5 3 6
                0 2
                1 2
                2 2
                3 2
                4 2
                4 0""";

        String expectedOutput = "2";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-aaaa")
                .languageId(1)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        executionService.execute(grade);
        Grade resultGrade = gradeRepository.findByToken("token-aaaa-aaaa-aaaa").get();

        assertThat(resultGrade.getStatusId()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER.getId());

    }

    @Test
    void 파일_만들기_테스트(){
        final String SOLUTIONFILEROOTDIR = "src/main/resources/";
        try {
            String filePath = SOLUTIONFILEROOTDIR + "token/" + "Main.java";
            File file = new File(filePath);
            if (file.createNewFile()){
                Files.writeString(Paths.get(filePath), "이건 소스코드야");
            } else {
                throw new RuntimeException("으아아아");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}