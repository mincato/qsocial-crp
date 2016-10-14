package com.qsocialnow.eventresolver.config;

public class CacheConfig {

    public static final String DOMAINS_CACHE = "domains";

    private Long maxSize;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

}
