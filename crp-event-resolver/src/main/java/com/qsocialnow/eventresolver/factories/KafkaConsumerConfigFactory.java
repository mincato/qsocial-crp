package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.kafka.config.KafkaConsumerConfig;

@Component
public class KafkaConsumerConfigFactory {

    @Autowired
    private CuratorFramework zookeeperClient;

    public KafkaConsumerConfig create(String kafkaConfigurationZnodePath) throws Exception {
        byte[] configBytes = zookeeperClient.getData().forPath(kafkaConfigurationZnodePath);
        KafkaConsumerConfig config = new GsonBuilder().create().fromJson(new String(configBytes),
                KafkaConsumerConfig.class);
        return config;

    }

}
