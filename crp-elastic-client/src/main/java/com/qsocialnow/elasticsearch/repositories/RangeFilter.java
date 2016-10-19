package com.qsocialnow.elasticsearch.repositories;

public class RangeFilter {

    private final String rangeField;

    private final String rangeFrom;

    private final String rangeTo;

    public RangeFilter(final String rangeField, final String rangeFrom, final String rangeTo) {
        this.rangeField = rangeField;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    public String getRangeField() {
        return rangeField;
    }

    public String getRangeFrom() {
        return rangeFrom;
    }

    public String getRangeTo() {
        return rangeTo;
    }
}
