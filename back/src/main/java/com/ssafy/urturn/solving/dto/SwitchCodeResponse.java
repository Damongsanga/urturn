package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SwitchCodeResponse {
    private String code;
    private int round;
    private Language language;
}
