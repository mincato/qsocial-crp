package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.AnalyticsConfig;

public class AnalyticsConfigFactory {

    public static AnalyticsConfig create(CuratorFramework zookeeperClient, String analyticsConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(analyticsConfigZnodePath);
        AnalyticsConfig config = new GsonBuilder().create()
                .fromJson(new String(configuratorBytes), AnalyticsConfig.class);
        return config;
    }

}
