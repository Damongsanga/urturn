package com.ssafy.urturn.grading.service;

import java.util.List;

public interface TokenCreator {

    String createToken();

    List<String> createTokens(int n);
}
