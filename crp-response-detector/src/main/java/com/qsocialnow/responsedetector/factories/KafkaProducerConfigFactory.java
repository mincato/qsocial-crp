package com.qsocialnow.responsedetector.factories;

import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;
import com.qsocialnow.kafka.config.KafkaProducerConfig;

public class KafkaProducerConfigFactory {

    public static KafkaProducerConfig create(CuratorFramework zookeeperClient, String kafkaConfigurationZnodePath)
            throws Exception {
        byte[] configBytes = zookeeperClient.getData().forPath(kafkaConfigurationZnodePath);
        KafkaProducerConfig config = new GsonBuilder().create().fromJson(new String(configBytes),
                KafkaProducerConfig.class);
        return config;

    }

}
