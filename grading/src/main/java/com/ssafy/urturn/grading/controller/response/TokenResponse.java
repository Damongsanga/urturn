package com.ssafy.urturn.grading.controller.response;

import com.ssafy.urturn.grading.domain.Grade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public static TokenResponse from(Grade grade){
        return new TokenResponse(grade.getToken());
    }
}
