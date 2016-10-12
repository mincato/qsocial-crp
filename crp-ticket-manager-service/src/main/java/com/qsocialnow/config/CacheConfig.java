package com.qsocialnow.config;

public class CacheConfig {

    public static final String THEMATICS = "thematics";

    public static final String CATEGORIES = "categories";

    public static final String USERS = "users";

    private Long maxSize;

    private Long expireAfterWrite;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(Long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }
}
