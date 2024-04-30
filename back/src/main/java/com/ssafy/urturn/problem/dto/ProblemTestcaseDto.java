package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.member.Level;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemTestcaseDto {

    private Long problemId;
    private String title;
    private String content;
    private Level level;
    private List<TestcaseDto> testcases;

}
