package com.qsocialnow.eventresolver.config;

public enum CacheEnum {

    DOMAINS_CACHE(CacheConfig.DOMAINS_CACHE), USER_RESOLVERS_CACHE(CacheConfig.USER_RESOLVERS_CACHE), USER_RESOLVERS_SOURCE_IDS_CACHE(
            CacheConfig.USER_RESOLVERS_SOURCE_IDS_CACHE), TEAMS_CACHE(CacheConfig.TEAMS_CACHE);

    private String value;

    private CacheEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
