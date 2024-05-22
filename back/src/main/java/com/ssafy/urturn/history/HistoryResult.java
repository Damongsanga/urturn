package com.ssafy.urturn.history;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryResult {
    SUCCESS("성공"), FAILURE("실패"), SURRENDER("포기");

    private String value;
}
