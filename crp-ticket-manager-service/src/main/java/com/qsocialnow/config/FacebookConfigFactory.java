package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.FacebookConfig;

public class FacebookConfigFactory {

    public static FacebookConfig findConfig(CuratorFramework zookeeperClient, String facebookConfiguratorZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(facebookConfiguratorZnodePath);
        FacebookConfig configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                FacebookConfig.class);
        return configurator;
    }

}
