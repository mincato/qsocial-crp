package com.qsocialnow.common.model.config;

public enum WordFilterType {
    TEXT("TEXTO"), AUTHOR("AUTORES"), HASHTAG("HASHTAGS"), MENTION("MENCIONES");

    private String name;

    private WordFilterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static WordFilterType getByName(String name) {
        for (WordFilterType wordFilterType : WordFilterType.values()) {
            if (wordFilterType.getName().equals(name)) {
                return wordFilterType;
            }
        }
        return null;
    }
}
