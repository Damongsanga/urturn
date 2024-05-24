package com.ssafy.urturn.global.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private JwtToken jwtToken;

}
