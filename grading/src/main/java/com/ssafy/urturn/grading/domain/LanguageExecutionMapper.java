package com.ssafy.urturn.grading.domain;

import org.springframework.stereotype.Component;

@Component
public class LanguageExecutionMapper {

    public static String getExecutionService(int languageId) {
        if (languageId == 62) {
            return "javaExecutionStrategy";
        } else if (languageId == 71) {
            return "pythonExecutionStrategy";
        } else if (languageId == 63) {
            return "javascriptExecutionStrategy";
        }  else if (languageId == 49) {
            return "CExecutionStrategy";
        } else if (languageId == 54) {
            return "cppExecutionStrategy";
        }  else {
            throw new IllegalArgumentException("Unsupported language ID: " + languageId);
        }

    }
}
