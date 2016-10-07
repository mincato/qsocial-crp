package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.exception.RepositoryException;
import com.qsocialnow.elasticsearch.queues.QueueConsumer;
import com.qsocialnow.elasticsearch.queues.QueueProducer;
import com.qsocialnow.elasticsearch.queues.QueueService;
import com.qsocialnow.elasticsearch.queues.QueueType;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.kafka.consumer.Consumer;
import com.qsocialnow.kafka.model.Message;

public class EventHandlerProcessor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventHandlerProcessor.class);

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
            try {
                messageProcessor.process(message);
            } catch (InvalidDomainException invalidDomainExecption) {
                addFailEvent(message);
            } catch (RepositoryException sourceException) {
                addFailEvent(message);
            } catch (Exception e) {
                log.error("Unexpected error: ", e);
                addDeadEvent(message);
            }
            message = consumer.consume();
        }
    }

    private void initQueueService() {
        if (queueService != null) {
            if (queueProducer == null) {
                queueProducer = new QueueProducer<Message>(QueueType.EVENTS.type());
                queueConsumer = new MessageQueueConsumer(QueueType.EVENTS.type(), messageProcessor, queueProducer);
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
}
