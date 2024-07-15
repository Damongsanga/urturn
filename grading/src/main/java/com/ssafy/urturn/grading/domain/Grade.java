package com.ssafy.urturn.grading.domain;

import com.ssafy.urturn.grading.dto.request.GradeCreateRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Grade {
    // db 저장 시
    private final int languageId;
    private final String stdout;
    private final int statusId;
    private final String stderr;
    private final String token;

    // token 생성시
    private final String sourceCode;
    private final String stdin;
    private final String expectedOutput;

    public static final Grade EMPTY = Grade.builder().build();

    public Grade setToken(String token){
        return Grade.builder()
                .languageId(this.languageId)
                .stdout(this.stdout)
                .statusId(this.statusId)
                .stderr(this.stderr)
                .token(token)
                .sourceCode(this.sourceCode)
                .stdin(this.stdin)
                .expectedOutput(this.expectedOutput)
                .build();
    }

    public Grade updateStatus(GradeStatus status){
        return Grade.builder()
                .languageId(this.languageId)
                .stdout(status.getDescription())
                .statusId(status.getId())
                .stderr(this.stderr)
                .token(this.token)
                .sourceCode(this.sourceCode)
                .stdin(this.stdin)
                .expectedOutput(this.expectedOutput)
                .build();
    }

    public Grade updateRuntimeErrorStatus(String errorMessage){
        return Grade.builder()
                .languageId(this.languageId)
                .stdout(errorMessage)
                .statusId(GradeStatus.RUNTIME_ERROR_OTHER.getId())
                .stderr(this.stderr)
                .token(this.token)
                .sourceCode(this.sourceCode)
                .stdin(this.stdin)
                .expectedOutput(this.expectedOutput)
                .build();
    }

    public static Grade from(GradeCreateRequest gradeCreateRequest){
        return Grade.builder()
                .sourceCode(gradeCreateRequest.sourceCode())
                .stdin(gradeCreateRequest.stdin())
                .expectedOutput(gradeCreateRequest.expectedOutput())
                .languageId(gradeCreateRequest.languageId())
                .statusId(GradeStatus.IN_QUEUE.getId())
                .build();
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public String getStrategyName(){
        return LanguageExecutionMapper.getExecutionService(this.languageId);
    }

    public static Grade empty(){
        return EMPTY;
    }

}
