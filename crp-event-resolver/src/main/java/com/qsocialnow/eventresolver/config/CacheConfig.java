package com.qsocialnow.eventresolver.config;

public class CacheConfig {

    public static final String DOMAINS_CACHE = "domains";

    public static final String USER_RESOLVERS_CACHE = "userResolvers";

    private Long maxSize;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

}
