package com.qsocialnow.autoscaling.factories;

import org.apache.curator.framework.CuratorFramework;

public class ZookeeperConnectionStringFactory {

    public static String create(CuratorFramework zookeeperClient, String zookeeperConfiguratorZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(zookeeperConfiguratorZnodePath);
        return new String(configuratorBytes);
    }

}
