package com.ssafy.urturn.grading.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.grading.controller.response.GradeResponse;
import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.domain.request.GradeGetRequest;
import com.ssafy.urturn.grading.mock.FakeGradeRepository;
import com.ssafy.urturn.grading.mock.TestTokenCreator;
import com.ssafy.urturn.grading.service.GradeQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        GradeGetRequest request = new GradeGetRequest(List.of("token1", "token2", "token3"));

        MvcResult result = mockMvc.perform(get("/submissions/batch")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        List<GradeResponse> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>(){});

        assertThat(responses.get(0).getToken()).isEqualTo("token1");
        assertThat(responses.get(0).getStdout()).isEqualTo("stdout1");
        assertThat(responses.get(0).getStatusId()).isEqualTo(1);

        assertThat(responses.get(1).getToken()).isEqualTo("token2");
        assertThat(responses.get(1).getStdout()).isEqualTo("stdout2");
        assertThat(responses.get(1).getStatusId()).isEqualTo(2);

        assertThat(responses.get(2).getToken()).isEqualTo("token3");
        assertThat(responses.get(2).getStdout()).isEqualTo("stdout3");
        assertThat(responses.get(2).getStatusId()).isEqualTo(3);


    }
}