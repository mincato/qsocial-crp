package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.elasticsearch.queues.QueueConsumer;
import com.qsocialnow.elasticsearch.queues.QueueProducer;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.kafka.model.Message;

public class MessageQueueConsumer extends QueueConsumer<Message> {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueConsumer.class);

    private MessageProcessor messageProcessor;

    private QueueProducer<Message> queueProducer;

    public MessageQueueConsumer(MessageProcessor messageProcessor, QueueProducer<Message> producer) {
        this.messageProcessor = messageProcessor;
        this.queueProducer = producer;
    }

    @Override
    public void process(Message message) {
        log.info("Processing message after dequeue it: " + message.getMessage());
        try {
            messageProcessor.process(message);
        } catch (InvalidDomainException invalidDomainExecption) {
            this.queueProducer.addItem(message);
        } catch (Exception e) {
            log.error("Unexpected retry process message error: ", e);
            this.queueProducer.addDeadItem(message);
        }
    }

    @Override
    public void save() {

    }

}
