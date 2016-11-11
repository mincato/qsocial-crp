package com.qsocialnow.config;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;

public class AWSLambdaClientConfigFactory {

    public static AWSLambdaClientConfig create(CuratorFramework zookeeperClient, String awsLambdaClientConfigZnodePath)
            throws Exception {
        byte[] configuratorBytes = zookeeperClient.getData().forPath(awsLambdaClientConfigZnodePath);
        AWSLambdaClientConfig config = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                AWSLambdaClientConfig.class);
        return config;
    }

}
