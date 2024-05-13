package com.ssafy.urturn.member.dto;

import com.ssafy.urturn.member.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProblemSimpleDto {

    private Long id;
    private String title;
    private Level level;

}
