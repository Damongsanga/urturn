package com.ssafy.urturn.grading.dto.response;

import com.ssafy.urturn.grading.domain.Grade;

public record TokenResponse (String token){
    public static TokenResponse from(Grade grade){
        return new TokenResponse(grade.getToken());
    }
}
