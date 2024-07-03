package com.ssafy.urturn.grading.controller.response;

import com.ssafy.urturn.grading.domain.Grade;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenResponse {
    private String token;

    public static TokenResponse from(Grade grade){
        return new TokenResponse(grade.getToken());
    }
}
