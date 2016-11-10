package com.qsocialnow.elasticsearch.repositories;

public class TermFieldFilter {

    private final String field;

    private final String value;

    private boolean needSplit;

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

    public boolean isNeedSplit() {
        return needSplit;
    }

    public void setNeedSplit(boolean needSplit) {
        this.needSplit = needSplit;
    }
}
