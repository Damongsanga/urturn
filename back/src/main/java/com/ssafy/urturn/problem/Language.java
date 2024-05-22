package com.ssafy.urturn.problem;

import lombok.Getter;

@Getter
public enum Language {

    JAVA(62), // OpenJDK 13.0.1
    CPP(54), //GCC 9.2.0
    C(50), //GCC 9.2.0
    JAVASCRIPT(63), //Node.js 12.14.0
    PYTHON(71); // 3.8.1

    private int langId;

    Language(int langId) {
        this.langId = langId;
    }
}
