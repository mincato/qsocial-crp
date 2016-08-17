package com.qsocialnow.kafka.admin;

import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.kafka.config.KafkaConfig;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

public class Administrator {

    private static final Logger log = LoggerFactory.getLogger(Administrator.class);

    private KafkaConfig kafkaConfig;

    public Administrator() {
    }

    public void createTopic(String zookeeperHosts, String name) {
        ZkClient zkClient = null;
        ZkUtils zkUtils = null;
        try {
            int sessionTimeOutInMs = kafkaConfig.getSessionTimeOutInMs();
            int connectionTimeOutInMs = kafkaConfig.getConnectionTimeOutInMs();

            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs,
                    ZKStringSerializer$.MODULE$);
            zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);

            int noOfPartitions = kafkaConfig.getNumberOfPartitionsForTopic();
            int noOfReplication = kafkaConfig.getNumberOfReplicationForTopic();
            Properties topicConfiguration = new Properties();

            AdminUtils.createTopic(zkUtils, name, noOfPartitions, noOfReplication, topicConfiguration);

        } catch (Exception ex) {
            log.error("Unexpected error", ex);
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

}
