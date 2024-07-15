package com.ssafy.urturn.grading.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // Grade
    RUN_CODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채점 프로세스 중 에러가 발생했습니다."),
    FILE_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 생성 중 에러가 발생하였습니다."),
    FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 에러가 발생하였습니다."),

    // Token
    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
