package com.ssafy.urturn.grading.dto.request;


import java.util.List;


public record GradeQueryRequest(
        List<String> tokens
){
}
