package com.ssafy.urturn.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GithubOAuthMemberInfoResponse extends OAuthMemberInfoResponse {

    @JsonProperty("id")
    private String oauthId;

    @JsonProperty("login")
    private String name;

    @JsonProperty("avatar_url")
    private String profileUrl;

    @JsonProperty("email")
    private String email;

}