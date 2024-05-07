package com.ssafy.urturn.global;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestLockType {

    GRADING(Duration.ofSeconds(100L));

    private Duration duration;
}
