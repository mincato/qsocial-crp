package com.qsocialnow.model;

import com.qsocialnow.common.util.FilterConstants;

public enum Connotation {

    POSITIVE(FilterConstants.CONNOTATION_POSITIVE, "plus"), NEGATIVE(FilterConstants.CONNOTATION_NEGATIVE, "minus"), NEUTRO(
            FilterConstants.CONNOTATION_NEUTRAL, "neutro");

    private Connotation(Short value, String icon) {
        this.value = value;
        this.icon = icon;
    }

    private Short value;

    private String icon;

    public Short getValue() {
        return value;
    }

    public void setValue(Short value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static Connotation getByValue(Short value) {
        for (Connotation connotation : Connotation.values()) {
            if (connotation.getValue().equals(value)) {
                return connotation;
            }
        }
        return null;
    }

}
