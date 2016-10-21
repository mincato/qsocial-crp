package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.security.LoginConfig;

public class LoginConfigFactory {

    public static LoginConfig create(CuratorFramework zookeeperClient, String loginConfigZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(loginConfigZnodePath);
        LoginConfig config = new GsonBuilder().create().fromJson(new String(configuratorBytes), LoginConfig.class);
        return config;
    }
}
