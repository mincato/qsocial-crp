package com.qsocialnow.kafka.config;

import com.qsocialnow.common.config.RefreshableConfig;

public class KafkaConsumerConfig implements RefreshableConfig<KafkaConsumerConfig> {

    private static final String DEFAULT_ZOOKEEPER_CONNECT = "localhost:2181";
    private static final String DEFAULT_ZOOKEEPER_SESSION_TIMEOUT_MS = "15000";
    private static final String DEFAULT_ZOOKEEPER_SYNC_TIMES_MS = "200";
    private static final String DEFAULT_AUTO_COMMIT_INTERVAL_MS = "1000";
    private static final String DEFAULT_TOPIC = "prc.domain1";
    private static final String DEFAULT_MESSAGE_CHARSET = "UTF-16";

    private String zookeeperConnect = DEFAULT_ZOOKEEPER_CONNECT;

    private String zookeeperSessionTimeoutMs = DEFAULT_ZOOKEEPER_SESSION_TIMEOUT_MS;

    private String zookeeperSyncTimeMs = DEFAULT_ZOOKEEPER_SYNC_TIMES_MS;

    private String autoCommitIntervalMs = DEFAULT_AUTO_COMMIT_INTERVAL_MS;

    private String topic = DEFAULT_TOPIC;

    private String messageCharset = DEFAULT_MESSAGE_CHARSET;

    public String getZookeeperConnect() {
        return zookeeperConnect;
    }

    public void setZookeeperConnect(String zookeeperConnect) {
        this.zookeeperConnect = zookeeperConnect;
    }

    public String getZookeeperSessionTimeoutMs() {
        return zookeeperSessionTimeoutMs;
    }

    public void setZookeeperSessionTimeoutMs(String zookeeperSessionTimeoutMs) {
        this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
    }

    public String getZookeeperSyncTimeMs() {
        return zookeeperSyncTimeMs;
    }

    public void setZookeeperSyncTimeMs(String zookeeperSyncTimeMs) {
        this.zookeeperSyncTimeMs = zookeeperSyncTimeMs;
    }

    public String getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageCharset() {
        return messageCharset;
    }

    public void setMessageCharset(String messageCharset) {
        this.messageCharset = messageCharset;
    }

    public static String getDefaultZookeeperConnect() {
        return DEFAULT_ZOOKEEPER_CONNECT;
    }

    public static String getDefaultZookeeperSessionTimeoutMs() {
        return DEFAULT_ZOOKEEPER_SESSION_TIMEOUT_MS;
    }

    public static String getDefaultZookeeperSyncTimesMs() {
        return DEFAULT_ZOOKEEPER_SYNC_TIMES_MS;
    }

    public static String getDefaultAutoCommitIntervalMs() {
        return DEFAULT_AUTO_COMMIT_INTERVAL_MS;
    }

    public static String getDefaultTopic() {
        return DEFAULT_TOPIC;
    }

    @Override
    public void refresh(KafkaConsumerConfig newConfig) {
        this.autoCommitIntervalMs = newConfig.autoCommitIntervalMs;
        this.topic = newConfig.topic;
        this.zookeeperConnect = newConfig.zookeeperConnect;
        this.zookeeperSessionTimeoutMs = newConfig.zookeeperSessionTimeoutMs;
        this.zookeeperSyncTimeMs = newConfig.zookeeperSyncTimeMs;
        this.messageCharset = newConfig.messageCharset;
    }

}
