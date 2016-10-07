package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.FacebookConfig;

public class FacebookConfigFactory {

    public static FacebookConfig create(CuratorFramework zookeeperClient, String facebookConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(facebookConfigZnodePath);
        FacebookConfig config = new GsonBuilder().create()
                .fromJson(new String(configuratorBytes), FacebookConfig.class);
        return config;
    }

}
