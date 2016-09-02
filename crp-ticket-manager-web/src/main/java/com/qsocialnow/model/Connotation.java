package com.qsocialnow.model;

import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.util.FilterConstants;

public enum Connotation {

    POSITIVE(FilterConstants.CONNOTATION_POSITIVE, "", "plus"), NEGATIVE(FilterConstants.CONNOTATION_NEGATIVE, "",
            "minus"), NEUTRO(FilterConstants.CONNOTATION_NEUTRAL, "", "neutro"), ALL((short) 0, Labels
            .getLabel("app.select.all"), "all");

    private Connotation(Short value, String label, String icon) {
        this.value = value;
        this.label = label;
        this.icon = icon;
    }

    private String label;

    private Short value;

    private String icon;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

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

}
