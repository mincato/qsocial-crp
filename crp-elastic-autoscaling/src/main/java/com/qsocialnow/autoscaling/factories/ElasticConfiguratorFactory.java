package com.qsocialnow.autoscaling.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.autoscaling.config.ConfigWatcher;
import com.qsocialnow.autoscaling.config.Configurator;


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
