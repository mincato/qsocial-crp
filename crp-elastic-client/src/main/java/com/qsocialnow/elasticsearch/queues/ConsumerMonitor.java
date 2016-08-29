package com.qsocialnow.elasticsearch.queues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMonitor<T> implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConsumerMonitor.class);

    private Consumer<T> consumerToMonitor;

    public ConsumerMonitor(final Consumer<T> consumer) {
        this.consumerToMonitor = consumer;
    }

    @Override
    public void run() {
        log.info("Monitor is notifing Consumer to read queue..");
        this.consumerToMonitor.nofityQueueReady();
    }

}
