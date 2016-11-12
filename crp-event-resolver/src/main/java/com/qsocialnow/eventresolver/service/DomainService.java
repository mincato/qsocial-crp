package com.qsocialnow.eventresolver.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.processor.MessageProcessorImpl;

@Component
public class DomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorImpl.class);

    private NodeCache domainsNodeCache;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private com.qsocialnow.elasticsearch.services.config.DomainService domainElasticService;

    @Cacheable(value = CacheConfig.DOMAINS_CACHE, unless = "#result == null")
    public Domain findDomainWithActiveTriggers(String domainId) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("======== Recovering Domain from Configuration Storage ========");
        }

        return domainElasticService.findDomainWithActiveTriggersAndActiveSegments(domainId);
    }

    @PostConstruct
    public void start() throws Exception {
        domainsNodeCache = new NodeCache(zookeeperClient, appConfig.getDomainsPath());
        domainsNodeCache.getListenable().addListener(new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                cacheManager.getCache(CacheConfig.DOMAINS_CACHE).clear();
            }
        });
        domainsNodeCache.start();
    }

    @PreDestroy
    public void close() {
        try {
            if (domainsNodeCache != null) {
                domainsNodeCache.close();
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error. Cause", e);
        }
    }

}
