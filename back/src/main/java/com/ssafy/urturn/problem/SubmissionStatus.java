package com.ssafy.urturn.problem;

import lombok.Getter;

@Getter
public enum SubmissionStatus {

    ALL_ACCEPTED(true), FAILED(false), PROCESSING(false);

    private boolean isSuccess;

    SubmissionStatus(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
