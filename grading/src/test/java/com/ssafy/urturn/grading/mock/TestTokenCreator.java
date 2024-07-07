package com.ssafy.urturn.grading.mock;

import com.ssafy.urturn.grading.service.TokenCreator;

import java.util.ArrayList;
import java.util.List;

public class TestTokenCreator implements TokenCreator {

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
}
