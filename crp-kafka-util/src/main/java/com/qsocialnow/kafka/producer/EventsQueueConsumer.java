package com.qsocialnow.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.queues.QueueConsumer;

public class EventsQueueConsumer extends QueueConsumer<String> {

    private static final Logger log = LoggerFactory.getLogger(EventsQueueConsumer.class);

    private Producer producer;

    public EventsQueueConsumer(String type, Producer producer) {
        super(type);
        this.producer = producer;
    }

    @Override
    public void process(String message) {
        log.debug("Processing messages after dequeue it: " + message);
        producer.send(message);
    }

    @Override
    public void save() {
    }

}
