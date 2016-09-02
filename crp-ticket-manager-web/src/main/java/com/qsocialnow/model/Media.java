package com.qsocialnow.model;

import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.util.FilterConstants;

public enum Media {

    FACEBOOK(FilterConstants.MEDIA_FACEBOOK, "", "facebook"), TWITTER(FilterConstants.MEDIA_TWITTER, "", "twitter"), ALL(
            0L, Labels.getLabel("app.select.all"), "all");

    private Media(Long value, String label, String icon) {
        this.value = value;
        this.label = label;
        this.icon = icon;
    }

    private String label;

    private Long value;

    private String icon;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

}
