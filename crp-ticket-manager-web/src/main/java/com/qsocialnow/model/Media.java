package com.qsocialnow.model;

import com.qsocialnow.common.util.FilterConstants;

public enum Media {

    FACEBOOK(FilterConstants.MEDIA_FACEBOOK, "facebook"), TWITTER(FilterConstants.MEDIA_TWITTER, "twitter");

    private Media(Long value, String icon) {
        this.value = value;
        this.icon = icon;
    }

    private Long value;

    private String icon;

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
