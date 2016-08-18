package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.kafka.consumer.Consumer;
import com.qsocialnow.kafka.model.Message;

public class EventHandlerProcessor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventHandlerProcessor.class);

    private final Consumer consumer;

    private final MessageProcessor messageProcessor;

    public EventHandlerProcessor(Consumer consumer, MessageProcessor messageProcessor) {
        this.consumer = consumer;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void run() {
        Message message = consumer.consume();
        while (message != null) {
            try {
                messageProcessor.process(message);
            } catch (Exception e) {
                log.error("Unexpected error: ", e);
            }
            message = consumer.consume();
        }
    }

    public void stop() {
        consumer.shutdown();
    }

}
