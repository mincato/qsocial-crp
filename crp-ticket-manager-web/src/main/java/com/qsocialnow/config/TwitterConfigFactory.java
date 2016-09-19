package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.TwitterConfig;

public class TwitterConfigFactory {

    public static TwitterConfig create(CuratorFramework zookeeperClient, String twitterConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(twitterConfigZnodePath);
        TwitterConfig config = new GsonBuilder().create().fromJson(new String(configuratorBytes), TwitterConfig.class);
        return config;
    }

}
