package com.ssafy.urturn.grading.dto.response;

import java.util.List;


public record GradeBatchResponse (
        List<GradeResponse> submissions
){
}
