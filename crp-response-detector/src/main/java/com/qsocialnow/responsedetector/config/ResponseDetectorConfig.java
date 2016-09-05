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
    
    @Value("${app.event.twitter.messages}")
    private String twitterMessagesPath;

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

	public String getTwitterMessagesPath() {
		return twitterMessagesPath;
	}

}
