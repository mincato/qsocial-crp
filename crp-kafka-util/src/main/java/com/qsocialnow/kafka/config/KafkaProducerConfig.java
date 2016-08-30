package com.qsocialnow.kafka.config;

import java.nio.charset.StandardCharsets;

public class KafkaProducerConfig {

    private static final String DEFAULT_KAFKA_SERVER = "localhost:9092";
    private static final String DEFAULT_ACKS = "all";
    private static final int DEFAULT_RETRIES = 0;
    private static final int DEFAULT_BATCH_SIZE = 16384;
    private static final int DEFAULT_LINGER_MS = 1;
    private static final int DEFAULT_BUFFER_MEMORY = 33554432;
    private static final String DEFAULT_VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String DEFAULT_VALUE_SERIALIZER_ENCODING = StandardCharsets.UTF_16.name();
    private static final String DEFAULT_TOPIC = "prc.domain1";

    private String kafkaServer = DEFAULT_KAFKA_SERVER;

    private String acks = DEFAULT_ACKS;

    private int retries = DEFAULT_RETRIES;

    private int batchSize = DEFAULT_BATCH_SIZE;

    private int lingerMs = DEFAULT_LINGER_MS;

    private int bufferMemory = DEFAULT_BUFFER_MEMORY;

    private String keySerializer = DEFAULT_VALUE_SERIALIZER;

    private String valueSerializer = DEFAULT_VALUE_SERIALIZER;

    private String valueSerializerEncoding = DEFAULT_VALUE_SERIALIZER_ENCODING;

    private String topic = DEFAULT_TOPIC;

    public String getKafkaServer() {
        return kafkaServer;
    }

    public void setKafkaServer(String kafkaServer) {
        this.kafkaServer = kafkaServer;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getValueSerializerEncoding() {
        return valueSerializerEncoding;
    }

    public void setValueSerializerEncoding(String valueSerializerEncoding) {
        this.valueSerializerEncoding = valueSerializerEncoding;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
