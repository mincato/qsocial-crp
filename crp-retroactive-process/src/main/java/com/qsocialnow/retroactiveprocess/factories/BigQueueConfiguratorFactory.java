package com.qsocialnow.retroactiveprocess.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.QueueConfigurator;

public class BigQueueConfiguratorFactory {

    public static QueueConfigurator getConfigurator(CuratorFramework zookeeperClient,
            String elasticConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
        QueueConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                QueueConfigurator.class);
        return configurator;
    }

}
