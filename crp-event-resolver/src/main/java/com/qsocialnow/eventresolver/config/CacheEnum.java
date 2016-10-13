package com.qsocialnow.eventresolver.config;

public enum CacheEnum {

    DOMAINS_CACHE(CacheConfig.DOMAINS_CACHE);

    private String value;

    private CacheEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
