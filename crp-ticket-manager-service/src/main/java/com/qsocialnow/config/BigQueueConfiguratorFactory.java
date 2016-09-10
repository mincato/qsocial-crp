package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;

@Component
public class BigQueueConfiguratorFactory {

    public static QueueConfigurator getConfigurator(CuratorFramework zookeeperClient,String elasticConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
        QueueConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                QueueConfigurator.class);
        return configurator;
    }

}
