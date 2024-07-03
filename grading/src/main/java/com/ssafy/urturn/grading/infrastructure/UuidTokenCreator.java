package com.ssafy.urturn.grading.infrastructure;

import com.ssafy.urturn.grading.service.TokenCreator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UuidTokenCreator implements TokenCreator {
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
}
