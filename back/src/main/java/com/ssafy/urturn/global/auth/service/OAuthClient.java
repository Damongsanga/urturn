package com.ssafy.urturn.global.auth.service;


import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.auth.dto.OAuthMemberInfoResponse;

public interface OAuthClient {
    OAuthAccessTokenResponse getAccessToken(String code);
    OAuthMemberInfoResponse getMemberInfo(OAuthAccessTokenResponse tokens);
}
