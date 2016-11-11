package com.qsocialnow.eventresolver.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.processor.MessageProcessorImpl;

@Component
public class TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorImpl.class);

    private PathChildrenCache pathChildrenCache;

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private com.qsocialnow.elasticsearch.services.config.TeamService teamElasticService;

    @Cacheable(value = CacheConfig.TEAMS_CACHE, unless = "#result == null")
    public Team findTeam(String teamId) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("======== Recovering Team from Configuration Storage ========");
        }

        return teamElasticService.findOne(teamId);
    }

    @PostConstruct
    public void start() throws Exception {
        pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getTeamsPath(), true);
        addListener();
        pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
    }

    @PreDestroy
    public void close() {
        try {
            if (pathChildrenCache != null) {
                LOGGER.info("Close team children cache");
                pathChildrenCache.close();
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error. Cause", e);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                    case CHILD_REMOVED:
                    case CHILD_UPDATED: {
                        String team = ZKPaths.getNodeFromPath(event.getData().getPath());
                        LOGGER.info("Removing team from cache: " + team);
                        cacheManager.getCache(CacheConfig.TEAMS_CACHE).evict(team);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

}
