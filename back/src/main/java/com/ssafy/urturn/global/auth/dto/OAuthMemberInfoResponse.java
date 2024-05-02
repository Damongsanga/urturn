package com.ssafy.urturn.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OAuthMemberInfoResponse {

    private String oauthId;

    private String name;

    private String profileUrl;

    private String email;

    private String accessToken;

}