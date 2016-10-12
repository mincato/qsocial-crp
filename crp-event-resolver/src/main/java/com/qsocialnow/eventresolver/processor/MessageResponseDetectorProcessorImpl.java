package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.filters.MessageFilter;
import com.qsocialnow.kafka.model.Message;

@Service
public class MessageResponseDetectorProcessorImpl implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageResponseDetectorProcessorImpl.class);

    @Autowired
    private DetectionMessageProcessor detectionMessageProcessor;

    @Autowired
    private ExecutionMessageProcessor executionMessageProcessor;

    @Autowired
    @Qualifier("messageResponseDetectorFilter")
    private MessageFilter messageFilter;

    public void process(Message message) throws Exception {
        // reintentar ES
        Event inputBeanDocument = new GsonBuilder().create().fromJson(message.getMessage(), Event.class);

        String domainId = message.getGroup();
        LOGGER.info(String.format("Processing message from Response Detector using domain %s: %s", domainId,
                inputBeanDocument));

        if (messageFilter.shouldProcess(inputBeanDocument, null)) {
            ExecutionMessageRequest executionMessageRequest = detectionMessageProcessor.detect(inputBeanDocument, null);
            if (executionMessageRequest != null) {
                executionMessageProcessor.execute(executionMessageRequest);
            } else {
                LOGGER.info(String.format("Message from Response Detect were not detected to execute an action: %s",
                        inputBeanDocument));
            }
        } else {
            LOGGER.info(String.format("Message should not be processed for this domain: %s", domainId));
        }
    }

    public void setDetectionMessageProcessor(DetectionMessageProcessor detectionMessageProcessor) {
        this.detectionMessageProcessor = detectionMessageProcessor;
    }

    public void setExecutionMessageProcessor(ExecutionMessageProcessor executionMessageProcessor) {
        this.executionMessageProcessor = executionMessageProcessor;
    }
}
