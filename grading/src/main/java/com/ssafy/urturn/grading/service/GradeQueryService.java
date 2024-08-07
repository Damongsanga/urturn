package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.global.exception.CustomException;
import com.ssafy.urturn.grading.global.exception.code.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.ssafy.urturn.grading.global.exception.code.CustomErrorCode.WRONG_TOKEN;

@Service
@RequiredArgsConstructor
public class GradeQueryService {

    private final GradeRepository gradeRepository;
    private final TokenManager tokenManager;

    public List<Grade> getGrades(String tokens) {

        List<String> tokenList = Arrays.asList(tokens.split(","));

        tokenList.forEach(token -> {
            if(tokenManager.isValidToken(token)) throw new CustomException(WRONG_TOKEN);
        });

        return tokenList.stream().map(gradeRepository::findByToken).
                map(optional -> optional.orElse(Grade.empty())).toList();
    }
}
