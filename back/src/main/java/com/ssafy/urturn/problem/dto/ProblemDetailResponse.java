package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.member.Level;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProblemDetailResponse {

    private Long id;
    private String title;
    private String content;
    private Level level;
    private List<TestcaseResponse> testcases;

}
