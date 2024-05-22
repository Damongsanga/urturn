package com.ssafy.urturn.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode{

    // 관련된 Error Code는 모아서 작성해주세요

    // Auth
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "해당 기능에 대한 권한이 없는 사용자입니다"),
    DUPLICATE_VALUE(HttpStatus.BAD_REQUEST, "중복 값이 허용되지 않습니다"),
    COOKIE_REFRESH_TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "refresh token이 존재하지 않습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "사용가능한 refresh token이 아닙니다"),
    NOT_VALID_USER(HttpStatus.UNAUTHORIZED, "사용자 권한이 유효하지 않습니다"),
    NO_USER(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보를 확인할 수 없습니다"),
    WRONG_ACCESS_WITHOUT_AUTHORIZATION(HttpStatus.FORBIDDEN, "비정상적인 접근입니다"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "token 암호화가 실패하였습니다"),
    DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "token 복호화가 실패하였습니다"),
    GITHUB_AUTHORIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GITHUB 정보를 얻어오는데 실패하였습니다"),

    // Member
    NO_MEMBER(HttpStatus.BAD_REQUEST, "ID에 해당하는 사용자가 존재하지 않습니다"),
    NO_REPOSITORY(HttpStatus.BAD_REQUEST, "해당 유저에게 github repository 정보가 존재하지 않습니다"),

    // History
    NO_HISTORY(HttpStatus.BAD_REQUEST, "ID에 해당하는 history가 존재하지 않습니다"),

    //Problem
    NO_PROBLEM(HttpStatus.BAD_REQUEST, "ID에 해당하는 문제가 존재하지 않습니다"),

    // Room
    NO_ROOM(HttpStatus.BAD_REQUEST, "ID에 해당하는 방이 존재하지 않습니다"),
    NO_ROOMINFO(HttpStatus.BAD_REQUEST, "방 정보를 가져올 수 없습니다."),
    CANNOT_ENTER_ROOM(HttpStatus.BAD_REQUEST, "방에 입장할 수 없습니다"),

    // Grading
    TOO_MUCH_TRAFFIC(HttpStatus.TOO_MANY_REQUESTS, "채점 서버가 많은 부하를 받고 있습니다. 잠시 후에 다시 시도해주세요!"),

    // Lock
    REQUEST_LOCKED(HttpStatus.LOCKED, "요청이 이미 진행중입니다. 잠시 후에 다시 요청해주세요");


    private final HttpStatus httpStatus;
    private final String message;
}
