package com.qsocialnow.common.model.config;

import com.qsocialnow.common.util.FilterConstants;

public enum Media {

    FACEBOOK(FilterConstants.MEDIA_FACEBOOK, "facebook", "Facebook"), TWITTER(FilterConstants.MEDIA_TWITTER, "twitter",
            "Twitter");

    private Media(Long value, String icon, String name) {
        this.value = value;
        this.icon = icon;
        this.name = name;
    }

    private Long value;

    private String icon;

    private String name;

    public Long getValue() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public static Media getByValue(Long value) {
        for (Media media : Media.values()) {
            if (media.getValue().equals(value)) {
                return media;
            }
        }
        return null;
    }

}