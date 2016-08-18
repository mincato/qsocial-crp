package com.qsocialnow.kafka.config;

import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class KafkaConfig {

    private final PropertiesConfiguration properties;

    private static final ThreadLocal<KafkaConfig> kafkaConfig = new ThreadLocal<KafkaConfig>() {

        @Override
        protected KafkaConfig initialValue() {
            // Load the default configuration file first
            Properties systemProperties = System.getProperties();
            String configProperty = systemProperties.getProperty("kafkaConfig");

            PropertiesConfiguration properties;
            try {
                properties = new PropertiesConfiguration(configProperty);
            } catch (ConfigurationException e) {
                throw new RuntimeException("Error loading configuration from " + configProperty);
            }

            return new KafkaConfig(properties);
        }
    };

    public KafkaConfig(PropertiesConfiguration properties) {
        this.properties = properties;
    }

    public KafkaConfig(String propertiesFile) {
        try {
            this.properties = new PropertiesConfiguration(propertiesFile);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error loading configuration from " + propertiesFile, e);
        }
    }

    public static KafkaConfig load() {
        return kafkaConfig.get();
    }

    public String getTopic() {
        return properties.getString("kafka.topic", "PRC");
    }

    public int getSessionTimeOutInMs() {
        return properties.getInt("kafka.zookeeper.sessionTimeoutInMs", 15 * 1000); // 15
                                                                                   // secs
    }

    public int getConnectionTimeOutInMs() {
        return properties.getInt("kafka.zookeeper.connectionTiemoutInMs", 10 * 1000); // 10
                                                                                      // secs
    }

    public int getNumberOfPartitionsForTopic() {
        return properties.getInt("kafka.topic.numberOfPartitions", 1);
    }

    public int getNumberOfReplicationForTopic() {
        return properties.getInt("kafka.topic.numberOfReplication", 1);
    }

    public int getSyncTimeInMs() {
        return properties.getInt("kafka.zookeeper.syncTimeInMs", 200);
    }

    public int getAutoCommitIntervalInMs() {
        return properties.getInt("kafka.autocommitIntervalInMs", 1000);
    }

}
