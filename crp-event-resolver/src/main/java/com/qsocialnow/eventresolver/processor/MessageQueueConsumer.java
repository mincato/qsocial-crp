package com.qsocialnow.eventresolver.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.kafka.model.Message;

public class MessageQueueConsumer extends QueueConsumer<Message> {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueConsumer.class);

    private MessageProcessor messageProcessor;

    private QueueProducer<Message> queueProducer;

    private static final AtomicInteger failsItemCount = new AtomicInteger(0);

    private static final int MAX_ALLOWED_RETRIES_COUNT = 10;

    private List<Message> documents = new ArrayList<Message>();

    public MessageQueueConsumer(String type, MessageProcessor messageProcessor, QueueProducer<Message> producer) {
        super(type);
        this.messageProcessor = messageProcessor;
        this.queueProducer = producer;
    }

    @Override
    public void process(Message message) {
        synchronized (documents) {
            log.debug("Adding to process event: " + message.getMessage());
            documents.add(message);
        }
    }

    @Override
    public void save() {
        synchronized (documents) {
            if (documents.size() > 0) {
                log.debug("Processing " + documents.size() + " messages after dequeue it");
                for (Message message : documents) {
                    try {
                        messageProcessor.process(message);
                        failsItemCount.set(0);
                    } catch (InvalidDomainException invalidDomainExecption) {
                        if (failsItemCount.get() < MAX_ALLOWED_RETRIES_COUNT) {
                            this.queueProducer.addItem(message);
                            failsItemCount.incrementAndGet();
                        } else {
                            log.error("Adding message items into dead queue after reach max retries allowed without recovery");
                            this.queueProducer.addDeadItem(message);
                        }
                    } catch (Exception e) {
                        log.error("Unexpected retry process message error: ", e);
                        this.queueProducer.addDeadItem(message);
                    }
                }
            }
        }
    }

}
