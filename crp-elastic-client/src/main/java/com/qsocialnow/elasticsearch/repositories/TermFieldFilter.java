package com.qsocialnow.elasticsearch.repositories;

public class TermFieldFilter {

    private final String field;

    private final String value;

    public TermFieldFilter(final String field, final String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
