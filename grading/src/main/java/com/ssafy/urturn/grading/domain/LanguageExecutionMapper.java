package com.ssafy.urturn.grading.domain;

import org.springframework.stereotype.Component;

@Component
public class LanguageExecutionMapper {

    public static String getExecutionService(int languageId) {
        if (languageId == 1) {
            return "javaExecutionStrategy";
        } else if (languageId == 2) {
            return "pythonExecutionStrategy";
        } else if (languageId == 3) {
            return "javascriptExecutionStrategy";
        } else {
            throw new IllegalArgumentException("Unsupported language ID: " + languageId);
        }

    }
}
