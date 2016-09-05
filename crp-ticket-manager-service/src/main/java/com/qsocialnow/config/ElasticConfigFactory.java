package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.elasticsearch.configuration.Configurator;

public class ElasticConfigFactory {

    public static Configurator findConfig(CuratorFramework zookeeperClient, String elasticConfiguratorZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
        Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                Configurator.class);
        return configurator;
    }

}
