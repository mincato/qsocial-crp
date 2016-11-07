package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.QueueConfigurator;
import com.qsocialnow.eventresolver.config.ConfigWatcher;

public class BigQueueConfiguratorFactory {

    public static QueueConfigurator getConfigurator(CuratorFramework zookeeperClient,
            String elasticConfiguratorZnodePath, ConfigWatcher<QueueConfigurator> configWather) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().usingWatcher(configWather)
                .forPath(elasticConfiguratorZnodePath);
        QueueConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                QueueConfigurator.class);
        return configurator;
    }

}
