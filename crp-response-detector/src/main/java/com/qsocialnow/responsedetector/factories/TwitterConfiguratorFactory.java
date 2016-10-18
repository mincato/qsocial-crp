package com.qsocialnow.responsedetector.factories;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;

@Component
public class TwitterConfiguratorFactory {

    @Autowired
    @Qualifier("zookeeperCentralClient")
    private CuratorFramework zookeeperClient;

    public TwitterConfigurator getConfigurator(String twitterConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(twitterConfiguratorZnodePath);
        TwitterConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                TwitterConfigurator.class);
        return configurator;
    }

}
