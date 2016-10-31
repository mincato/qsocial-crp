package com.qsocialnow.model;

import com.qsocialnow.common.util.FilterConstants;

public enum Connotation {

    POSITIVE(FilterConstants.CONNOTATION_POSITIVE, "plus", FilterConstants.CONNOTATION_POSITIVE_NAME), NEGATIVE(
            FilterConstants.CONNOTATION_NEGATIVE, "minus", FilterConstants.CONNOTATION_NEGATIVE_NAME), NEUTRO(
            FilterConstants.CONNOTATION_NEUTRAL, "neutro", FilterConstants.CONNOTATION_NEUTRAL_NAME);

    private Connotation(Short value, String icon, String name) {
        this.value = value;
        this.icon = icon;
        this.name = name;
    }

    private Short value;

    private String icon;

    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Connotation getByName(String name) {
        for (Connotation connotation : Connotation.values()) {
            if (connotation.getName().equals(name)) {
                return connotation;
            }
        }
        return null;
    }

}
