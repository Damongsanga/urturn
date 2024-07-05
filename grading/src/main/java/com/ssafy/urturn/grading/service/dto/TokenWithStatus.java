package com.ssafy.urturn.grading.service.dto;

import com.ssafy.urturn.grading.domain.GradeStatus;
import lombok.Getter;

@Getter
public class TokenWithStatus {
    private final String token;
    private final GradeStatus status;

    private TokenWithStatus(String token, GradeStatus status) {
        this.token = token;
        this.status = status;
    }

    public static TokenWithStatus from(String token, GradeStatus status){
        return new TokenWithStatus(token, status);
    }
}
