package com.qsocialnow.elasticsearch.queues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueConsumerMonitor<T> implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(QueueConsumerMonitor.class);

    private QueueConsumer<T> consumerToMonitor;
    
    public QueueConsumerMonitor(final QueueConsumer<T> consumer) {
        this.consumerToMonitor = consumer;
    }

    @Override
    public void run() {
        log.info("Monitor is notifing Consumer of "+consumerToMonitor.getType()+" to read queue");
        this.consumerToMonitor.nofityQueueReady();
    }

}
