package com.qsocialnow.common.model.config;

import java.util.Set;

public class WordFilter {

    private WordFilterType type;

    private Set<String> text;

    private String inputText;

    public Set<String> getText() {
        return text;
    }

    public void setText(Set<String> text) {
        this.text = text;
    }

    public WordFilterType getType() {
        return type;
    }

    public void setType(WordFilterType type) {
        this.type = type;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

}
