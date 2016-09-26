package com.qsocialnow.eventresolver.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.RefreshableConfig;

public class ConfigWatcher<T extends RefreshableConfig<T>> implements CuratorWatcher {

    private static final Logger log = LoggerFactory.getLogger(ConfigWatcher.class);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private CuratorFramework zookeeperClient;

    private String beanRef;

    private Class<T> clazz;

    public ConfigWatcher(String beanRef, Class<T> clazz) {
        this.beanRef = beanRef;
        this.clazz = clazz;
    }

    @Override
    public void process(WatchedEvent watcher) {
        EventType type = watcher.getType();
        switch (type) {
            case NodeDataChanged:
                log.info("Refreshing config for bean: " + beanRef);
                try {
                    T config = (T) appContext.getBean(beanRef, clazz);
                    byte[] configBytes = zookeeperClient.getData().usingWatcher(this).forPath(watcher.getPath());
                    T newConfig = new GsonBuilder().create().fromJson(new String(configBytes), clazz);
                    config.refresh(newConfig);
                } catch (Exception e) {
                    log.error("There was an error updating kafka config", e);
                }
                break;

            default:
                break;
        }

    }

}
