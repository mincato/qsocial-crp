package com.qsocialnow.kafka.consumer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.qsocialnow.kafka.config.KafkaConfig;
import com.qsocialnow.kafka.exceptions.EncodingException;
import com.qsocialnow.kafka.model.Message;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class Consumer {

    private ConsumerConnector consumer;

    private KafkaConfig kafkaConfig;

    private ConsumerIterator<byte[], byte[]> streamIterator;

    private String group;

    public Consumer(String zookeeperPath, KafkaConfig kafkaConfig, String group) {
        this.kafkaConfig = kafkaConfig;
        this.group = group;
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeperPath, group));
        HashMap<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(kafkaConfig.getTopic(), 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumer.createMessageStreams(topicCountMap);
        this.streamIterator = streams.get(kafkaConfig.getTopic()).get(0).iterator();

    }

    private ConsumerConfig createConsumerConfig(String zookeeperPath, String group) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperPath);
        props.put("group.id", group);
        props.put("zookeeper.session.timeout.ms", Integer.toString(kafkaConfig.getSessionTimeOutInMs()));
        props.put("zookeeper.sync.time.ms", Integer.toString(kafkaConfig.getSyncTimeInMs()));
        props.put("auto.commit.interval.ms", Integer.toString(kafkaConfig.getAutoCommitIntervalInMs()));
        return new ConsumerConfig(props);
    }

    public Message consume() {

        if (streamIterator.hasNext()) {
            try {
                MessageAndMetadata<byte[], byte[]> currentMessage = streamIterator.next();
                Message message = new Message();
                message.setMessage(new String(currentMessage.message(), "UTF-16"));
                message.setGroup(group);
                return message;
            } catch (UnsupportedEncodingException e) {
                throw new EncodingException(e.getMessage());
            }
        } else {
            return null;
        }

    }

    public void shutdown() {
        if (consumer != null)
            consumer.shutdown();
    }

}
