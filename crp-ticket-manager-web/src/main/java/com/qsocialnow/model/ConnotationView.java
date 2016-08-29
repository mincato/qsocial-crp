package com.qsocialnow.model;

import org.zkoss.util.resource.Labels;

public enum ConnotationView {

    POSITIVE("1", "", "plus"), NEGATIVE("2", "", "minus"), NEUTRO("3", "", "neutro"), ALL("ALL", Labels
            .getLabel("app.select.all"), "all");

    private ConnotationView(String value, String label, String icon) {
        this.value = value;
        this.label = label;
        this.icon = icon;
    }

    private String label;

    private String value;

    private String icon;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
