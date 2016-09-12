package com.qsocialnow.eventresolver.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.eventresolver.config.ConfigWatcher;
import com.qsocialnow.kafka.config.KafkaConsumerConfig;

public class KafkaConsumerConfigFactory {

    public static KafkaConsumerConfig create(CuratorFramework zookeeperClient, String kafkaConfigurationZnodePath,
            ConfigWatcher<KafkaConsumerConfig> kafkaConfigWather) throws Exception {
        byte[] configBytes = zookeeperClient.getData().usingWatcher(kafkaConfigWather)
                .forPath(kafkaConfigurationZnodePath);
        KafkaConsumerConfig config = new GsonBuilder().create().fromJson(new String(configBytes),
                KafkaConsumerConfig.class);
        return config;

    }

}
