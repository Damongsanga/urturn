package com.ssafy.urturn.solving.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class submitResponse {
    // 정답 유무
    private boolean result;
    // 사용자에게 표시하고 싶은 메시지.
    private String message;
}
