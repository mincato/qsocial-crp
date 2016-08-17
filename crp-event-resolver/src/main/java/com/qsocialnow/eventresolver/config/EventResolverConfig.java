package com.qsocialnow.eventresolver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class EventResolverConfig {

    @Value("${app.zookeeper.host}")
    private String zookeeperHost;

    @Value("${app.kafka.zookeeper.host.path}")
    private String kafkaZookeeerZnodePath;

    @Value("${app.dateFormat:dd/MM/yyyy}")
    private String dateFormat;

    @Value("${app.elastic.config.configurator.path}")
    private String elasticConfigConfiguratorZnodePath;

    @Value("${app.elastic.cases.configurator.path}")
    private String elasticCasesConfiguratorZnodePath;

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public String getKafkaZookeeerZnodePath() {
        return kafkaZookeeerZnodePath;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getElasticConfigConfiguratorZnodePath() {
        return elasticConfigConfiguratorZnodePath;
    }

    public String getElasticCasesConfiguratorZnodePath() {
        return elasticCasesConfiguratorZnodePath;
    }

}
