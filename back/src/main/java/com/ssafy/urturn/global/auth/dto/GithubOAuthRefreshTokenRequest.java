package com.ssafy.urturn.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubOAuthRefreshTokenRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public GithubOAuthRefreshTokenRequest(String clientId, String clientSecret, String grantType,
        String refreshToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.refreshToken = refreshToken;
    }
}
