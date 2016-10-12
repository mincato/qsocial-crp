package com.qsocialnow.config;

public enum CacheEnum {

    THEMATICS_CACHE(CacheConfig.THEMATICS), CATEGORIES_CACHE(CacheConfig.CATEGORIES), USERS_CACHE(CacheConfig.USERS);

    private String value;

    private CacheEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
