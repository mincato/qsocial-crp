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

    private static final int TOTAL_ITEM_COUNTS = 3;

    private static final int TOTAL_FAIL_ITEM_COUNTS = 10;

    private static final int TOTAL_MAX_DEAD_ITEM_COUNTS = 30;

    private static final int DELAY = 1;

    private static final int FAIL_DELAY = 3;

    private static final int INITIAL_DELAY = 1;

    private static final String DEAD_LETTER_QUEUE_DIR = "deadLetterQueue";

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    private final String baseDir;

    private final String queueDir;

    private final String queueFailDir;

    private final String deadLetterQueueDir;

    private static IBigQueue bigQueue;

    private static IBigQueue bigQueueFail;

    private static IBigQueue deadLetterQueue;

    private static QueueService instance;

    private QueueService(QueueConfigurator queueConfiguration) {
        this.baseDir = queueConfiguration.getBaseDir();
        this.queueDir = this.baseDir + queueConfiguration.getQueueDir();
        this.queueFailDir = this.baseDir + queueConfiguration.getErrorQueueDir();
        this.deadLetterQueueDir = this.queueFailDir + DEAD_LETTER_QUEUE_DIR;
    }

    public static QueueService getInstance(QueueConfigurator queueConfiguration) {
        if (instance == null)
            instance = new QueueService(queueConfiguration);

        return instance;
    }

    public boolean initQueue(String type) {
        boolean isQueueReady = false;
        try {
            if (bigQueue == null) {
                bigQueue = new BigQueueImpl(queueDir, type);
                log.info("Creating queue:" + type + " successfully..");
            }
            isQueueReady = true;

        } catch (IOException e) {
            log.error("Error trying to create queues:", e);
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
                log.info("Creating dead letter queue:" + type + " successfully..");
            }
            isQueueReady = true;

        } catch (IOException e) {
            log.error("Error trying to create fail - error queues:", e);
        }
        return isQueueReady;
    }

    public <T> void startConsumer(Consumer<T> consumer) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        consumer.setDelay(DELAY);
        consumer.setInitialDelay(INITIAL_DELAY);
        consumer.setTotalItemCounts(TOTAL_ITEM_COUNTS);
        consumer.setQueue(bigQueue);
        service.execute(consumer);
    }

    public <T> void startProducer(Producer<T> producer) {
        producer.setQueue(bigQueue);
        producer.setTotalItemCounts(TOTAL_ITEM_COUNTS);
        log.info("Starting producer");
        producer.start();
    }

    public <T> void startFailConsumer(Consumer<T> consumer) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        consumer.setDelay(FAIL_DELAY);
        consumer.setInitialDelay(INITIAL_DELAY);
        consumer.setTotalItemCounts(TOTAL_FAIL_ITEM_COUNTS);
        consumer.setQueue(bigQueueFail);
        service.execute(consumer);
    }

    public <T> void startFailProducer(Producer<T> producer) {
        producer.setQueue(bigQueueFail);
        producer.setTotalItemCounts(TOTAL_FAIL_ITEM_COUNTS);
        producer.setTotalMaxDeadItemCounts(TOTAL_MAX_DEAD_ITEM_COUNTS);
        producer.setDeadLetterQueue(deadLetterQueue);
        log.info("Starting fail producer");
        producer.start();
    }
}
