package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;

public class TwitterConfigFactory {

    public static TwitterConfigurator findConfig(CuratorFramework zookeeperClient, String twitterConfiguratorZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(twitterConfiguratorZnodePath);
        TwitterConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                TwitterConfigurator.class);
        return configurator;
    }

}
