package com.ssafy.urturn.problem;

import lombok.Getter;

@Getter
public enum Language {

    JAVA(62);

    private int langId;

    Language(int langId) {
        this.langId = langId;
    }
}
