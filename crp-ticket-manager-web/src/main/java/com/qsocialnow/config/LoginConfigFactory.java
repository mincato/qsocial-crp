package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.FacebookConfig;
import com.qsocialnow.security.LoginConfig;

public class LoginConfigFactory {

    public static LoginConfig create(CuratorFramework zookeeperClient, String facebookConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(facebookConfigZnodePath);
        LoginConfig config = new GsonBuilder().create()
                .fromJson(new String(configuratorBytes), FacebookConfig.class);
        return config;
    }	
}
