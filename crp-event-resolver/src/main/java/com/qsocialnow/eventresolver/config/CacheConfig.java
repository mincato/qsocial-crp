package com.qsocialnow.eventresolver.config;

public class CacheConfig {

    public static final String DOMAINS_CACHE = "domains";

    public static final String USER_RESOLVERS_CACHE = "userResolvers";

    public static final String USER_RESOLVERS_SOURCE_IDS_CACHE = "userResolversSourceIds";

    public static final String TEAMS_CACHE = "teams";

    private Long maxSize;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

}
