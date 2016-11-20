package com.qsocialnow.common.model.config;

import com.qsocialnow.common.util.FilterConstants;

public enum Media {

    FACEBOOK(FilterConstants.MEDIA_FACEBOOK, "facebook", "Facebook", 0), TWITTER(FilterConstants.MEDIA_TWITTER,
            "twitter", "Twitter", 140), MANUAL(-1L, "manual", "Manual", 0);

    private Media(Long value, String icon, String name, Integer maxlength) {
        this.value = value;
        this.icon = icon;
        this.name = name;
        this.maxlength = 140;
    }

    private Long value;

    private String icon;

    private String name;

    private Integer maxlength;

    public Long getValue() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxlength() {
        return maxlength;
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