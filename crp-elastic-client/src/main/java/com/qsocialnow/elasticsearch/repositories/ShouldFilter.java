package com.qsocialnow.elasticsearch.repositories;

public class ShouldFilter {

    private final String field;

    private final String value;

    public ShouldFilter(final String field, final String value) {
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
