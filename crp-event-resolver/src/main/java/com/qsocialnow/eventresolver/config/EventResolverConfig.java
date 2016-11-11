package com.qsocialnow.eventresolver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class EventResolverConfig {

    @Value("${app.zookeeper.host}")
    private String zookeeperHost;

    @Value("${app.kafka.config.znode.path}")
    private String kafkaConfigZnodePath;

    @Value("${app.elastic.config.configurator.path}")
    private String elasticConfigConfiguratorZnodePath;

    @Value("${app.elastic.cases.configurator.path}")
    private String elasticCasesConfiguratorZnodePath;

    @Value("${app.cases.queue.configurator.path}")
    private String casesQueueConfiguratorZnodePath;

    @Value("${app.events.queue.configurator.path}")
    private String eventsQueueConfiguratorZnodePath;

    @Value("${app.domains.path}")
    private String domainsPath;

    @Value("${app.user.resolvers.path}")
    private String userResolversPath;

    @Value("${app.teams.path}")
    private String teamsPath;

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public String getKafkaConfigZnodePath() {
        return kafkaConfigZnodePath;
    }

    public String getElasticConfigConfiguratorZnodePath() {
        return elasticConfigConfiguratorZnodePath;
    }

    public String getElasticCasesConfiguratorZnodePath() {
        return elasticCasesConfiguratorZnodePath;
    }

    public String getDomainsPath() {
        return domainsPath;
    }

    public String getCasesQueueConfiguratorZnodePath() {
        return casesQueueConfiguratorZnodePath;
    }

    public String getEventsQueueConfiguratorZnodePath() {
        return eventsQueueConfiguratorZnodePath;
    }

    public String getUserResolversPath() {
        return userResolversPath;
    }

    public String getTeamsPath() {
        return teamsPath;
    }

    public void setTeamsPath(String teamsPath) {
        this.teamsPath = teamsPath;
    }

}
