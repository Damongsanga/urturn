package com.ssafy.urturn.grading.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.grading.controller.response.TokenResponse;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.domain.request.GradeCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class TokenControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GradeRepository gradeRepository;

    @AfterEach
    void clear(){
        gradeRepository.deleteAll();
    }

    @Test
    void 정답_코드를_반환받는다() throws Exception {

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

        GradeCreate gradeCreate = GradeCreate.builder()
                .sourceCode(sourceCode)
                .languageId(1)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .build();

        List<GradeCreate> gradeCreates = new ArrayList<>();
        gradeCreates.add(gradeCreate);

        MvcResult result = mockMvc.perform(post("/submissions/batch")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(gradeCreates)))
                .andExpect(status().isOk())
                .andReturn();

        List<TokenResponse> tokenResponses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        String createdToken = tokenResponses.get(0).getToken();

        assertThat(tokenResponses.size()).isEqualTo(1);
        assertThat(gradeRepository.findByToken(createdToken).get().getStatusId()).isEqualTo(1);
    }

    @Test
    void 소스코드_인풋_예상아웃풋은_빈칸일_수_없다() throws Exception {

        String sourceCode = "";
        String stdIn = "1";
        String expectedOutput = "1";

        GradeCreate gradeCreate = GradeCreate.builder()
                .sourceCode(sourceCode)
                .languageId(1)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .build();

        List<GradeCreate> gradeCreates = new ArrayList<>();
        gradeCreates.add(gradeCreate);

        mockMvc.perform(post("/submissions/batch")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(gradeCreates)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void language_id는_1에서_62사이_값이다() throws Exception {

        String sourceCode = "sourceCode";
        String stdIn = "1";
        String expectedOutput = "1";

        GradeCreate gradeCreate = GradeCreate.builder()
                .sourceCode(sourceCode)
                .languageId(1)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .build();

        GradeCreate gradeCreateLow = GradeCreate.builder()
                .sourceCode(sourceCode)
                .languageId(0)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .build();

        GradeCreate gradeCreateHigh = GradeCreate.builder()
                .sourceCode(sourceCode)
                .languageId(63)
                .stdin(stdIn)
                .expectedOutput(expectedOutput)
                .build();

        List<GradeCreate> gradeCreateLows = List.of(gradeCreateLow, gradeCreate);

        mockMvc.perform(post("/submissions/batch")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(gradeCreateLows)))
                .andExpect(status().is4xxClientError());

        List<GradeCreate> gradeCreateHighs = List.of(gradeCreateHigh, gradeCreate);

        mockMvc.perform(post("/submissions/batch")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(gradeCreateHighs)))
                .andExpect(status().is4xxClientError());
    }
}
