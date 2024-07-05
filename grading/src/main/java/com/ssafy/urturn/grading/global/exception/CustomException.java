package com.ssafy.urturn.grading.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
