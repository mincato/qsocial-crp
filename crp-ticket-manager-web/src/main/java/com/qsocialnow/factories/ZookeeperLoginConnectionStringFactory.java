package com.qsocialnow.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.security.LoginConfig;

public class ZookeeperLoginConnectionStringFactory {

    public static String create(CuratorFramework zookeeperClient, String zookeeperConfiguratorZnodePath)
            throws Exception {
        byte[] data = zookeeperClient.getData().forPath(zookeeperConfiguratorZnodePath);
        LoginConfig config = new GsonBuilder().create().fromJson(new String(data), LoginConfig.class);
        return config.getZookeeperHost();
    }

}
