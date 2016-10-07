package com.qsocialnow.elasticsearch.queues;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;

public class QueueService {

    private static final int TOTAL_ITEM_COUNTS = 20;

    private static final int TOTAL_FAIL_ITEM_COUNTS = 40;

    private static final int TOTAL_MAX_DEAD_ITEM_COUNTS = 120;

    private static final int DELAY = 10;

    private static final int FAIL_DELAY = 10;

    private static final int INITIAL_DELAY = 30;

    private static final String DEAD_LETTER_QUEUE_DIR = "/deadLetterQueue";

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    private ExecutorService serviceProducerConsumer;

    private ExecutorService serviceFailProducerConsumer;

    private final String baseDir;

    private final String queueDir;

    private final String queueFailDir;

    private final String deadLetterQueueDir;

    private IBigQueue bigQueue;

    private IBigQueue bigQueueFail;

    private IBigQueue deadLetterQueue;

    private String type;

    // private static QueueService instance;

    QueueService(QueueConfigurator queueConfiguration) {
        this.baseDir = queueConfiguration.getBaseDir();
        this.queueDir = this.baseDir + queueConfiguration.getQueueDir();
        this.queueFailDir = this.baseDir + queueConfiguration.getErrorQueueDir();
        this.deadLetterQueueDir = this.queueFailDir + DEAD_LETTER_QUEUE_DIR;
    }

    public boolean initQueue(String type) {
        boolean isQueueReady = false;
        this.type = type;
        try {
            if (bigQueue == null) {
                bigQueue = new BigQueueImpl(queueDir, type);
                log.info("Creating queue:" + type + " successfully..");
            }
            isQueueReady = true;

        } catch (IOException e) {
            log.error("Error trying to create queues type: " + this.type, e);
        }
        return isQueueReady;
    }

    public boolean initFailQueue(String type) {
        boolean isQueueReady = false;
        try {
            if (bigQueueFail == null) {
                bigQueueFail = new BigQueueImpl(queueFailDir, type);
                log.info("Creating fail queue:" + type + " successfully..");
            }
            if (deadLetterQueue == null) {
                deadLetterQueue = new BigQueueImpl(deadLetterQueueDir, type);
                log.info("Creating dead letter queue type: " + this.type + " successfully..");
            }
            isQueueReady = true;

        } catch (IOException e) {
            log.error("Error trying to create fail - error queues:", e);
        }
        return isQueueReady;
    }

    public <T> void startProducerConsumer(QueueProducer<T> producer, QueueConsumer<T> consumer) {
        serviceProducerConsumer = Executors.newFixedThreadPool(2);

        consumer.setDelay(DELAY);
        consumer.setInitialDelay(INITIAL_DELAY);
        consumer.setTotalItemCounts(TOTAL_ITEM_COUNTS);
        consumer.setQueue(bigQueue);
        log.info("Starting consumer queue for type :" + this.type);
        producer.setQueue(bigQueue);
        producer.setTotalItemCounts(TOTAL_ITEM_COUNTS);
        log.info("Starting producer queue for type :" + this.type);
        serviceProducerConsumer.execute(consumer);
        serviceProducerConsumer.execute(producer);
    }

    public <T> void startFailProducerConsumer(QueueProducer<T> producer, QueueConsumer<T> consumer) {
        serviceFailProducerConsumer = Executors.newFixedThreadPool(2);
        consumer.setDelay(FAIL_DELAY);
        consumer.setInitialDelay(INITIAL_DELAY);
        consumer.setTotalItemCounts(TOTAL_FAIL_ITEM_COUNTS);
        consumer.setQueue(bigQueueFail);
        producer.setQueue(bigQueueFail);
        producer.setTotalItemCounts(TOTAL_FAIL_ITEM_COUNTS);
        producer.setTotalMaxDeadItemCounts(TOTAL_MAX_DEAD_ITEM_COUNTS);
        producer.setDeadLetterQueue(deadLetterQueue);
        log.info("Starting fail consumer queue for type :" + this.type);
        serviceFailProducerConsumer.execute(consumer);
        log.info("Starting fail producer queue for type :" + this.type);
        serviceFailProducerConsumer.execute(producer);
    }

    public void shutdownQueueService() {
        try {
            if (serviceProducerConsumer != null) {
                serviceProducerConsumer.shutdown();
                serviceProducerConsumer.awaitTermination(10L, TimeUnit.SECONDS);
            }
            if (serviceFailProducerConsumer != null) {
                serviceFailProducerConsumer.shutdown();
                serviceFailProducerConsumer.awaitTermination(10L, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            log.error("Unexpected error trying to shutdown queue service. Cause", e);
        }

    }
}
