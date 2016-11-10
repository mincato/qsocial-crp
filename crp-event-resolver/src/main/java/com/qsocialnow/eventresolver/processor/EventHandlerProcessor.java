package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.exception.RepositoryException;
import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.eventresolver.App;
import com.qsocialnow.eventresolver.Consumers;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.kafka.consumer.Consumer;
import com.qsocialnow.kafka.model.Message;

public class EventHandlerProcessor implements Runnable {

    private static final Logger ROOT_LOGGER = LoggerFactory.getLogger(EventHandlerProcessor.class);

    private static final Logger EVENT_LOGGER = LoggerFactory.getLogger("eventLogger");

    private final Consumer consumer;

    private final MessageProcessor messageProcessor;

    private final QueueService queueService;

    private QueueProducer<Message> queueProducer;

    private QueueConsumer<Message> queueConsumer;

    public EventHandlerProcessor(Consumer consumer, MessageProcessor messageProcessor, QueueService queueService) {
        this.consumer = consumer;
        this.messageProcessor = messageProcessor;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        initQueueService();
        Message message = consumer.consume();
        while (message != null) {
            logEvent(message);
            try {
                messageProcessor.process(message);
            } catch (InvalidDomainException invalidDomainExecption) {
                ROOT_LOGGER.error("There was an error processing event", invalidDomainExecption);
                addFailEvent(message);
            } catch (RepositoryException sourceException) {
                ROOT_LOGGER.error("There was an error processing event", sourceException);
                addFailEvent(message);
            } catch (Exception e) {
                ROOT_LOGGER.error("Unexpected error: ", e);
                addDeadEvent(message);
            }
            message = consumer.consume();
        }
    }

    private void initQueueService() {
        if (queueService != null) {
            if (queueProducer == null) {
                queueProducer = new QueueProducer<Message>(queueService.getType());
                queueConsumer = new MessageQueueConsumer(queueService.getType(), messageProcessor, queueProducer);
                queueProducer.addConsumer(queueConsumer);
                queueService.startFailProducerConsumer(queueProducer, queueConsumer);
            }
        }
    }

    private void addFailEvent(Message message) {
        queueProducer.addItem(message);
    }

    private void addDeadEvent(Message message) {
        queueProducer.addDeadItem(message);
    }

    public void stop() {
        consumer.shutdown();
        queueProducer.stopProducer();
        queueConsumer.stopConsumer();
        queueService.shutdownQueueService();
    }

    private void logEvent(Message message) {
        try {
            if (message.getGroup().equals(Consumers.RESPONSE_DETECTOR_CONSUMER_NAME)) {
                EVENT_LOGGER.info(message.getMessage());
            }
        } catch (Exception e) {
        }
    }
}
