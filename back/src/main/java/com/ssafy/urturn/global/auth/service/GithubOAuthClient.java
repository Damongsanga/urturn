package com.ssafy.urturn.global.auth.service;


import com.ssafy.urturn.global.auth.dto.GithubOAuthAccessTokenRequest;
import com.ssafy.urturn.global.auth.dto.GithubOAuthMemberInfoResponse;
import com.ssafy.urturn.global.auth.dto.GithubOAuthRefreshTokenRequest;
import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.auth.dto.OAuthMemberInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubOAuthClient implements OAuthClient {

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String MEMBER_INFO_URL = "https://api.github.com/user";
    private static final RestTemplate restTemplate = new RestTemplate();
    private final String clientId;
    private final String clientSecret;

    public GithubOAuthClient(@Value("${spring.security.oauth2.client.registration.github.client-id}") String clientId,
                             @Value("${spring.security.oauth2.client.registration.github.client-secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public OAuthAccessTokenResponse getAccessToken(String code) {
        return restTemplate.postForObject(
                ACCESS_TOKEN_URL,
                new GithubOAuthAccessTokenRequest(clientId, clientSecret, code),
                OAuthAccessTokenResponse.class
                );
    }

    @Override
    public OAuthMemberInfoResponse getMemberInfo(OAuthAccessTokenResponse tokens) {
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(tokens.getAccessToken());
        GithubOAuthMemberInfoResponse res = restTemplate.exchange(
                MEMBER_INFO_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GithubOAuthMemberInfoResponse.class
        ).getBody();

        res.setAccessToken(tokens.getAccessToken());
        return res;
    }


}
