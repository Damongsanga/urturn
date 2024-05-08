package com.ssafy.urturn.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.entity.Testcase;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemTestcaseDto {

    @JsonProperty("algoQuestionId")
    private Long problemId;

    @JsonProperty("algoQuestionTitle")
    private String title;

    @JsonProperty("algoQuestionUrl")
    private String content;

    @JsonIgnore
    private Level level;

    private List<TestcaseDto> testcases;

    public ProblemTestcaseDto(Problem problem, List<TestcaseDto> testcases){
        this.problemId = problem.getId();
        this.title = problem.getTitle();
        this.content = problem.getContent();
        this.level = problem.getLevel();
        this.testcases = testcases;
    }

}
