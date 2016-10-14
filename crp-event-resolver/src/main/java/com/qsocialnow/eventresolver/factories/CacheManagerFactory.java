package com.qsocialnow.eventresolver.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;

import com.google.common.cache.CacheBuilder;
import com.google.gson.GsonBuilder;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.config.CacheEnum;

public class CacheManagerFactory {

    public static CacheManager create(CuratorFramework zookeeperClient, Map<CacheEnum, String> cacheConfigZnodePaths)
            throws Exception {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<GuavaCache> caches = new ArrayList<>();
        for (Map.Entry<CacheEnum, String> configZnodePath : cacheConfigZnodePaths.entrySet()) {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(configZnodePath.getValue());
            CacheConfig config = new GsonBuilder().create().fromJson(new String(configuratorBytes), CacheConfig.class);
            GuavaCache guavaCache = new GuavaCache(configZnodePath.getKey().getValue(), CacheBuilder.newBuilder()
                    .maximumSize(config.getMaxSize()).build());
            caches.add(guavaCache);
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
