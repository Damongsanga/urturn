package com.ssafy.urturn.grading.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GradeBatchResponse {

    private List<GradeResponse> submissions;

    public GradeBatchResponse(List<GradeResponse> submissions) {
        this.submissions = submissions;
    }
}
