package com.qsocialnow.common.queues;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.qsocialnow.common.config.QueueConfigurator;

public class QueueService {

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

    private QueueConfigurator queueConfiguration;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // private static QueueService instance;

    QueueService(QueueConfigurator queueConfiguration) {
        this.queueConfiguration = queueConfiguration;
        this.baseDir = queueConfiguration.getBaseDir();
        this.queueDir = this.baseDir + queueConfiguration.getQueueDir();
        this.queueFailDir = this.baseDir + queueConfiguration.getErrorQueueDir();
        this.deadLetterQueueDir = this.queueFailDir + queueConfiguration.getDeadLetterQueueDir();
    }

    public boolean initQueue(String type) {
        boolean isQueueReady = false;
        this.type = type;
        try {
            if (bigQueue == null) {
                bigQueue = new BigQueueImpl(queueDir, type);
                log.info("Creating queue:" + type + " successfully..");
                startCleanMonitor();
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

        consumer.setDelay(queueConfiguration.getDelay());
        consumer.setInitialDelay(queueConfiguration.getInitialDelay());
        consumer.setTotalItemCounts(queueConfiguration.getTotalItemCounts());
        consumer.setQueue(bigQueue);
        log.info("Starting consumer queue for type :" + this.type);
        producer.setQueue(bigQueue);
        producer.setTotalItemCounts(queueConfiguration.getTotalItemCounts());
        log.info("Starting producer queue for type :" + this.type);
        serviceProducerConsumer.execute(consumer);
        serviceProducerConsumer.execute(producer);
    }

    public <T> void startFailProducerConsumer(QueueProducer<T> producer, QueueConsumer<T> consumer) {
        serviceFailProducerConsumer = Executors.newFixedThreadPool(2);
        consumer.setDelay(queueConfiguration.getDelay());
        consumer.setInitialDelay(queueConfiguration.getInitialDelay());
        consumer.setTotalItemCounts(queueConfiguration.getTotalItemCounts());
        consumer.setQueue(bigQueueFail);
        producer.setQueue(bigQueueFail);
        producer.setTotalItemCounts(queueConfiguration.getTotalItemCounts());
        producer.setTotalMaxDeadItemCounts(queueConfiguration.getTotalMaxDeadItemCounts());
        producer.setDeadLetterQueue(deadLetterQueue);
        log.info("Starting fail consumer queue for type :" + this.type);
        serviceFailProducerConsumer.execute(consumer);
        log.info("Starting fail producer queue for type :" + this.type);
        serviceFailProducerConsumer.execute(producer);
    }

    private void startCleanMonitor() {
        if (!executor.isShutdown()) {
            log.info("Starting monitor to clean files for queue - type:" + this.type);
            executor.scheduleWithFixedDelay(new CleanQueuesMonitor(), queueConfiguration.getCleanInitialDelay(),
                    queueConfiguration.getCleanDelay(), TimeUnit.SECONDS);
        }
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
            if (executor != null) {
                executor.shutdown();
            }
            if (bigQueue != null) {
                log.debug("gc de big queue for queue - type:" + this.type);
                bigQueue.gc();
                bigQueue.close();
            }
            if (bigQueueFail != null) {
                bigQueueFail.gc();
                bigQueueFail.close();
            }
            if (deadLetterQueue != null) {
                deadLetterQueue.gc();
                deadLetterQueue.close();
            }
        } catch (Exception e) {
            log.error("Unexpected error trying to shutdown queue service. Cause", e);
        }

    }

    public String getType() {
        return type;
    }

    class CleanQueuesMonitor implements Runnable {

        @Override
        public void run() {
            try {
                if (bigQueue != null) {
                    log.debug("gc de big queue for queue - type:" + type);
                    bigQueue.gc();
                }
                if (bigQueueFail != null) {
                    bigQueueFail.gc();
                }
                if (deadLetterQueue != null) {
                    deadLetterQueue.gc();
                }
            } catch (Exception e) {
                log.error("There was an error cleaning queues", e);
            }

        }

    }

}
