package com.ssafy.urturn.problem.dto;

import com.ssafy.urturn.problem.Language;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenBatchCreateDto {

    private List<TokenCreateDto> submissions;

    public TokenBatchCreateDto(List<TestcaseDto> testcases, String sourceCode, Language language){
        this.submissions = new ArrayList<>();
        for (TestcaseDto testcase : testcases) {
            submissions.add(new TokenCreateDto(testcase, sourceCode, language));
        }
    }

}
