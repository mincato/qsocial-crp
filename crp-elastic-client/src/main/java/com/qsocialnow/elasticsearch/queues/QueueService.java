package com.qsocialnow.elasticsearch.queues;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;

public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    private final String queueDir;

    private static IBigQueue bigQueue;

    private static QueueService instance;

    private QueueService(QueueConfigurator queueConfiguration) {
        this.queueDir = queueConfiguration.getQueueDir();
    }

    public static QueueService getInstance(QueueConfigurator queueConfiguration, QueueType queueType) {
        if (instance == null)
            instance = new QueueService(queueConfiguration);
        instance.initQueue(queueType.type());

        return instance;
    }

    private void initQueue(String type) {
        if (bigQueue == null) {
            try {
                bigQueue = new BigQueueImpl(queueDir, type);
                log.info("Creating queue successfully..");

            } catch (IOException e) {
                log.error("Error trying to create the queue:", e);
            }
        }
    }

    public <T> void startConsumer(Consumer<T> consumer) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        consumer.setQueue(bigQueue);
        service.execute(consumer);
    }

    public <T> void startProducer(Producer<T> producer) {
        producer.setQueue(bigQueue);
        log.info("Starting producer");
        producer.start();
    }
}
