package com.qsocialnow.kafka.producer;

import java.util.List;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.kafka.config.KafkaProducerConfig;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private org.apache.kafka.clients.producer.Producer<String, String> producer;

    private KafkaProducerConfig kafkaConfig;

    public Producer() {
        this(new KafkaProducerConfig());
    }

    public Producer(KafkaProducerConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public void start() {
        producer = new KafkaProducer<>(createProducerConfig(kafkaConfig));
    }

    private Properties createProducerConfig(KafkaProducerConfig kafkaConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfig.getKafkaServer());
        props.put("acks", kafkaConfig.getAcks());
        props.put("retries", kafkaConfig.getRetries());
        props.put("batch.size", kafkaConfig.getBatchSize());
        props.put("linger.ms", kafkaConfig.getLingerMs());
        props.put("buffer.memory", kafkaConfig.getBufferMemory());
        props.put("key.serializer", kafkaConfig.getKeySerializer());
        props.put("value.serializer", kafkaConfig.getValueSerializer());
        props.put("value.serializer.encoding", kafkaConfig.getValueSerializerEncoding());
        return props;
    }

    public void send(List<String> messages) {
        if (CollectionUtils.isNotEmpty(messages)) {
            for (String message : messages) {
                ProducerRecord<String, String> record = new ProducerRecord<>(kafkaConfig.getTopic(), message);
                producer.send(record, new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata record, Exception ex) {
                        if (ex != null) {
                            log.error("There was an error sending the record", ex);
                            // TODO meter en bigqueue? Si es asi, hacerlo en
                            // otro thread para no bloquear
                        }
                    }
                });
            }
        }
    }

    public void close() {
        if (producer != null)
            producer.close();
    }

}
