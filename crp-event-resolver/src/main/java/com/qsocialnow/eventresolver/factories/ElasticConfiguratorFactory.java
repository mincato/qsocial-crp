package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.eventresolver.config.ConfigWatcher;

public class ElasticConfiguratorFactory {

    public static Configurator getConfigurator(CuratorFramework zookeeperClient, String elasticConfiguratorZnodePath,
            ConfigWatcher<Configurator> configWatcher) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().usingWatcher(configWatcher)
                .forPath(elasticConfiguratorZnodePath);
        Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                Configurator.class);
        return configurator;
    }

}
