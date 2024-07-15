package com.ssafy.urturn.grading.mock;

import com.ssafy.urturn.grading.service.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class TestTokenManager implements TokenManager {

    private int id;
    private final String fakeUUID = "fake-token-";

    @Override
    public String createToken() {
        return fakeUUID + ++id;
    }

    @Override
    public List<String> createTokens(int n) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tokens.add(fakeUUID + ++id);
        }
        return tokens;
    }

    @Override
    public boolean isValidToken(String token) {
        return token.equals(fakeUUID);
    }
}
