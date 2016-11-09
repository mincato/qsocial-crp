package com.qsocialnow.common.services;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceService {

    private static final Logger log = LoggerFactory.getLogger(SourceService.class);

    private CuratorFramework zookeeperClient;

    private PathChildrenCache blockedSourcesNodeCache;

    private String blockedSourcesZnodePath;

    public void start() throws Exception {
        blockedSourcesNodeCache = new PathChildrenCache(zookeeperClient, blockedSourcesZnodePath, false);
        blockedSourcesNodeCache.start(StartMode.BUILD_INITIAL_CACHE);
    }

    public void stop() {
        if (blockedSourcesNodeCache != null) {
            try {
                blockedSourcesNodeCache.close();
            } catch (IOException e) {
                log.error("There was an unexpected error closing cache for blocked sources", e);
            }
        }
    }

    public void blockSource(Long sourceId) {
        try {
            String path = blockedSourcesZnodePath.concat("/" + sourceId.toString());
            Stat stat = zookeeperClient.checkExists().forPath(path);
            if (stat == null) {
                zookeeperClient.create().forPath(path);
            }
        } catch (Exception e) {
            log.error("There was an exception trying to block source", e);
        }
    }

    public Set<Long> getBlockedSources() {
        List<ChildData> currentData = blockedSourcesNodeCache.getCurrentData();
        return currentData.stream()
                .map(data -> Long.parseLong(data.getPath().substring(data.getPath().lastIndexOf("/") + 1)))
                .collect(Collectors.toSet());

    }

    public void setZookeeperClient(CuratorFramework zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public void setBlockedSourcesZnodePath(String blockedSourcesZnodePath) {
        this.blockedSourcesZnodePath = blockedSourcesZnodePath;
    }
}
