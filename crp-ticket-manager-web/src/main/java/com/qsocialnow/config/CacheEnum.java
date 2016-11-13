package com.qsocialnow.config;

public enum CacheEnum {

    ORGANIZATIONS_CACHE(CacheConfig.ORGANIZATIONS);

    private String value;

    private CacheEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
