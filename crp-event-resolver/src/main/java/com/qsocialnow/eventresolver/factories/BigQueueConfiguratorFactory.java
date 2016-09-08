package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;

@Component
public class BigQueueConfiguratorFactory {

    @Autowired
    private CuratorFramework zookeeperClient;

    public QueueConfigurator getConfigurator(String elasticConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
        QueueConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                QueueConfigurator.class);
        return configurator;
    }

}
