package com.ssafy.urturn.grading.global.exception;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.urturn.grading.global.exception.code.CommonErrorCode;
import com.ssafy.urturn.grading.global.exception.code.ErrorCode;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 커스텀된 에러 리턴을 위한 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomArgument(CustomException e) {
        log.warn("CustomException : {}", e.getMessage());
        log.warn("CustomException : {}", (Object) e.getStackTrace());
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument : {}", e.getMessage());
        log.warn("handleIllegalArgument : {}", (Object) e.getStackTrace());
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    // Validation에 의한 MethodArgumentNotValidException은 ResponseEntityExceptionHandler에서 처리하나 여기에 메시지를 넣어줄 수 있도록 커스터마이징하자
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("handleIllegalArgument :{}", e.getMessage());
        log.warn("handleIllegalArgument :{}", (Object) e.getStackTrace());
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("handleAllException : {}", e.getMessage());
        log.warn("handleAllException : {}", (Object) e.getStackTrace());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode, "data 제한조건에 위반되는 요청입니다");
    }

    // 이외 에러들은 internal error로 처리한다 (NPE등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.warn("handleAllException : {}", e.getMessage());
        log.warn("handleAllException : {}", (Object) e.getStackTrace());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(message)
                        .build());
    }

    // @Valid에 의한 validation 발생 시 에러가 발생한 필드 정보를 담은 Response 반환
    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    // @Valid에 의한 validation 발생 시 에러가 발생한 필드 정보를 담은 Response 반환
    private Object makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream().map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }

    @Builder
    private record ErrorResponse (
        String code,
        String message,
        // 에러가 없을 경우 응답에서 제외
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ValidationError> errors
    ){
        // @Valid에 의한 에러 발생시 어떤 필드에서 에러 발생햇는지에 대한 응답
        @Builder
        private record ValidationError(String field, String message) {
            public static ValidationError of(final FieldError fieldError) {
                return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
    }
}
