package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.elasticsearch.configuration.Configurator;

@Component
public class ElasticConfiguratorFactory {

    public static Configurator getConfigurator(CuratorFramework zookeeperClient,String elasticConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
        Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                Configurator.class);
        return configurator;
    }

}
