package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.TwitterConfig;

public class TwitterConfigFactory {

    public static TwitterConfig findConfig(CuratorFramework zookeeperClient, String twitterConfiguratorZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(twitterConfiguratorZnodePath);
        TwitterConfig configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                TwitterConfig.class);
        return configurator;
    }

}
