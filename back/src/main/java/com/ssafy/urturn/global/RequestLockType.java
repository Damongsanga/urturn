package com.ssafy.urturn.global;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestLockType {

    // 채점 100초 이상 진행되면 다시 요청할 수 있음
    GRADING(Duration.ofSeconds(100L));

    private Duration duration;
}
