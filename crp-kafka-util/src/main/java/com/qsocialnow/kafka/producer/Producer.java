package com.qsocialnow.kafka.producer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.kafka.config.KafkaProducerConfig;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private org.apache.kafka.clients.producer.Producer<String, String> producer;

    private KafkaProducerConfig kafkaConfig;

    private ExecutorService queueExecutor;

    private QueueService queueService;

    private QueueProducer<String> queueProducer;

    private QueueConsumer<String> queueConsumer;

    public Producer(KafkaProducerConfig kafkaConfig, QueueService queueService) {
        this.kafkaConfig = kafkaConfig;
        this.queueService = queueService;
    }

    public void start() {
        producer = new KafkaProducer<>(createProducerConfig(kafkaConfig));
        queueExecutor = Executors.newCachedThreadPool();
        initQueues();
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
                send(message);
            }
        }
    }

    public void send(String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(kafkaConfig.getTopic(), message);
        producer.send(record, new Callback() {

            @Override
            public void onCompletion(RecordMetadata record, Exception ex) {
                if (ex != null) {
                    log.error("There was an error sending the record", ex);

                    queueExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            if (queueProducer != null) {
                                queueProducer.addItem(message);
                            }

                        }
                    });
                } else {
                    log.debug("The record has been sent successfully");
                }
            }
        });

    }

    private void initQueues() {
        if (queueService != null) {
            if (queueProducer == null) {
                queueProducer = new QueueProducer<String>(queueService.getType());
                queueConsumer = new EventsQueueConsumer(queueService.getType(), this);
                queueProducer.addConsumer(queueConsumer);
                queueService.startFailProducerConsumer(queueProducer, queueConsumer);
            }
        }
    }

    public void close() {
        if (producer != null)
            producer.close();
        if (queueExecutor != null) {
            queueExecutor.shutdown();
        }
    }

}
