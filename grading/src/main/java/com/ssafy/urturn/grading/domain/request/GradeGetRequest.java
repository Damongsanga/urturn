package com.ssafy.urturn.grading.domain.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GradeGetRequest {

    private List<String> tokens;

    public GradeGetRequest(List<String> tokens) {
        this.tokens = tokens;
    }
}
