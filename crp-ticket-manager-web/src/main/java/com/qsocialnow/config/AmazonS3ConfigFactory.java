package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;

public class AmazonS3ConfigFactory {

    public static AmazonS3Config create(CuratorFramework zookeeperClient, String amazonS3ConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(amazonS3ConfigZnodePath);
        AmazonS3Config config = new GsonBuilder().create()
                .fromJson(new String(configuratorBytes), AmazonS3Config.class);
        return config;
    }

}
