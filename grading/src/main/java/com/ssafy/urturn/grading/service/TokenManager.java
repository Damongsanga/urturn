package com.ssafy.urturn.grading.service;

import java.util.List;

public interface TokenManager {

    String createToken();

    List<String> createTokens(int n);

    boolean isValidToken(String token);
}
