package com.ssafy.urturn.grading.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.grading.dto.response.GradeBatchResponse;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ResultControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void init(){
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

        gradeRepository.save(grade1);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);

    }

    @Test
    void 값을_조회할_수_있다() throws Exception {


        MvcResult result = mockMvc.perform(get("/submissions/batch?tokens=token1,token2,token3")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        GradeBatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>(){});

        assertThat(response.submissions().get(0).token()).isEqualTo("token1");
        assertThat(response.submissions().get(0).stdout()).isEqualTo("stdout1");
        assertThat(response.submissions().get(0).statusId()).isEqualTo(1);

        assertThat(response.submissions().get(1).token()).isEqualTo("token2");
        assertThat(response.submissions().get(1).stdout()).isEqualTo("stdout2");
        assertThat(response.submissions().get(1).statusId()).isEqualTo(2);

        assertThat(response.submissions().get(2).token()).isEqualTo("token3");
        assertThat(response.submissions().get(2).stdout()).isEqualTo("stdout3");
        assertThat(response.submissions().get(2).statusId()).isEqualTo(3);


    }

    @Test
    void tokens_구분자를_validatae_할_수_있다() throws Exception {
        MvcResult result = mockMvc.perform(get("/submissions/batch?tokens=token1.token2,token3&field=123&base64_encoded=true")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        GradeBatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertThat(new String(Base64.getDecoder().decode(response.submissions().get(0).stderr()), StandardCharsets.UTF_8)).isEqualTo("잘못된 토큰입니다.");

    }
}