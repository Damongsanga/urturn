package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.Grade;
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
class JavaExecutionStrategyTest {

    @Autowired
    ExecutionStrategy javaExecutionStrategy;
    @Autowired
    GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정답을_반환할_수_있다() {

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
                .token("token-aaaa-aaaa-java-accepted")
                .languageId(62)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javaExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.ACCEPTED);

    }

    @Test
    void 오답을_반환할_수_있다() {
        // given
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

        String expectedOutput = "10000";

        Grade grade = Grade.builder()
                .token("token-aaaa-aaaa-java-wrong")
                .languageId(62)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javaExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.WRONG_ANSWER);

    }

    @Test
    void 컴파일_에러를_반환할_수_있다() {

        // given
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
                          청      // 해당 위치에 배추가 있으며 아직 방문하지 않았다면 DFS 재귀 호출
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
                .token("token-aaaa-aaaa-java-compile-error")
                .languageId(62)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javaExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.COMPILATION_ERROR);

    }

    @Test
    void 런타임_에러를_반환할_수_있다(){

        // given
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
                .token("token-aaaa-aaaa-runtime")
                .languageId(62)
                .sourceCode(sourceCode)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
        gradeRepository.save(grade);

        CompletableFuture<TokenWithStatus> future = javaExecutionStrategy.execute(grade);
        TokenWithStatus res = future.join();
        assertThat(res.getStatus()).isEqualTo(GradeStatus.RUNTIME_ERROR_OTHER);

    }

    @Test
    void 중복_요청에_대해_정상적으로_동작한다() {

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


        List<Grade> grades = new ArrayList<>();

        for (int idx = 0; idx < 10; idx++) {
            Grade grade = Grade.builder()
                    .token("token-aaaa-aaaa-java-" + idx)
                    .languageId(62)
                    .sourceCode(sourceCode)
                    .stdin(stdIn)
                    .expectedOutput(expectedOutput)
                    .statusId(GradeStatus.IN_QUEUE.getId())
                    .build();
            grades.add(grade);
            gradeRepository.save(grade);
        }

        grades.stream().map(javaExecutionStrategy::execute).forEach(
                future -> assertThat(future.join().getStatus()).isEqualTo(GradeStatus.ACCEPTED)
        );

    }

}