package com.ssafy.urturn.grading.infrastructure;

import com.ssafy.urturn.grading.service.TokenManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UuidTokenManager implements TokenManager {
    @Override
    public String createToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<String> createTokens(int n) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tokens.add(UUID.randomUUID().toString());
        }
        return tokens;
    }

    @Override
    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            UUID uuid = UUID.fromString(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
