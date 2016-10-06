package com.qsocialnow.responsedetector.factories;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.responsedetector.config.FacebookConfigurator;

@Component
public class FacebookConfiguratorFactory {

    @Autowired
    private CuratorFramework zookeeperClient;

    public FacebookConfigurator getConfigurator(String facebookConfiguratorZnodePath) throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(facebookConfiguratorZnodePath);
        FacebookConfigurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
        		FacebookConfigurator.class);
        return configurator;
    }

}
