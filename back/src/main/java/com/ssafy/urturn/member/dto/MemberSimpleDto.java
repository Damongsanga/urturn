package com.ssafy.urturn.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberSimpleDto {

    private Long id;
    private String nickname;
    private String profileImage;

}
