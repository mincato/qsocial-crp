package com.qsocialnow.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public void clean() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.stream().forEach(cacheString -> cacheManager.getCache(cacheString).clear());
    }

}
