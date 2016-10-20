package com.qsocialnow.retroactiveprocess.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.retroactiveprocess.config.PagedEventsServiceConfig;

public class PagedEventsServiceConfigFactory {

    public static PagedEventsServiceConfig create(CuratorFramework zookeeperClient,
            String pagedEventsServiceConfigurationZnodePath) throws Exception {
        byte[] configBytes = zookeeperClient.getData().forPath(pagedEventsServiceConfigurationZnodePath);
        PagedEventsServiceConfig config = new GsonBuilder().create().fromJson(new String(configBytes),
                PagedEventsServiceConfig.class);
        return config;

    }

}
