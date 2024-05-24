package com.ssafy.urturn.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다"),
    WRONG_REQUEST(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스가 존재하지 않습니다"),
    DUPLICATE_VALUE(HttpStatus.BAD_REQUEST, "중복 값이 허용되지 않습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다"),
    JSON_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "json 변환에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
