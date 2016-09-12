package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.kafka.config.KafkaConsumerConfig;

public class KafkaConsumerConfigFactory {

    public static KafkaConsumerConfig create(CuratorFramework zookeeperClient, String kafkaConfigurationZnodePath)
            throws Exception {
        byte[] configBytes = zookeeperClient.getData().forPath(kafkaConfigurationZnodePath);
        KafkaConsumerConfig config = new GsonBuilder().create().fromJson(new String(configBytes),
                KafkaConsumerConfig.class);
        return config;

    }

}
