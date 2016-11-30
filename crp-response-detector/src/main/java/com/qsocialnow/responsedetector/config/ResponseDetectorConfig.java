package com.qsocialnow.responsedetector.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class ResponseDetectorConfig {

    @Value("${app.zookeeper.host}")
    private String zookeeperHost;

    @Value("${app.elastic.config.configurator.path}")
    private String elasticConfigConfiguratorZnodePath;

    @Value("${app.elastic.cases.configurator.path}")
    private String elasticCasesConfiguratorZnodePath;

    @Value("${app.twitter.app.configurator.path}")
    private String twitterAppConfiguratorZnodePath;

    @Value("${app.facebook.app.configurator.path}")
    private String facebookAppConfiguratorZnodePath;

    @Value("${app.twitter.users.path}")
    private String twitterUsersZnodePath;

    @Value("${app.facebook.conversations.path}")
    private String facebookConversationsZnodePath;

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public String getElasticConfigConfiguratorZnodePath() {
        return elasticConfigConfiguratorZnodePath;
    }

    public String getElasticCasesConfiguratorZnodePath() {
        return elasticCasesConfiguratorZnodePath;
    }

    public String getTwitterAppConfiguratorZnodePath() {
        return twitterAppConfiguratorZnodePath;
    }

    public String getTwitterUsersZnodePath() {
        return twitterUsersZnodePath;
    }

    public String getFacebookAppConfiguratorZnodePath() {
        return facebookAppConfiguratorZnodePath;
    }

    public String getFacebookConversationsZnodePath() {
        return facebookConversationsZnodePath;
    }

}
