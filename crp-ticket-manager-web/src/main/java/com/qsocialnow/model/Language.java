package com.qsocialnow.model;

import com.qsocialnow.common.util.FilterConstants;

public enum Language {

    ENGLISH(FilterConstants.LANGUAGE_ENGLISH, "english"), SPANISH(FilterConstants.LANGUAGE_SPANISH, "spanish"), PORTUGUESE(
            FilterConstants.LANGUAGE_PORTUGUESE, "portuguese");

    private Language(String value, String image) {
        this.value = value;
        this.image = image;
    }

    private String value;

    private String image;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }
}
