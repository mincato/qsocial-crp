package com.qsocialnow.factories;

import org.apache.curator.framework.CuratorFramework;

public class AdminUnitServiceConfigFactory {

    public static String create(CuratorFramework zookeeperClient, String adminUnitServiceConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(adminUnitServiceConfigZnodePath);
        String config = new String(configuratorBytes);
        return config;
    }

}
