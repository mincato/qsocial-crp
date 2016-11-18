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
        if (inputBeanDocument == null) {
        	return;
        }

        String domainId = message.getGroup();
        logProcessingEvent(inputBeanDocument, domainId);

        if (messageFilter.shouldProcess(inputBeanDocument, null)) {
            ExecutionMessageRequest executionMessageRequest = detectionMessageProcessor.detect(inputBeanDocument, null);
            if (executionMessageRequest != null) {
                executionMessageProcessor.execute(executionMessageRequest);
            } else {
                logMessageNotDetected(inputBeanDocument);
            }
        } else {
            LOGGER.debug(String.format("Message should not be processed for this domain: %s", domainId));
        }
    }

    private void logMessageNotDetected(Event inputBeanDocument) {
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug(String.format("Message from Response Detect were not detected to execute an action: %s",
                        inputBeanDocument.getId()));
                LOGGER.debug(String.format("Message: %s", inputBeanDocument));
            } catch (Exception e) {
            }
        }
    }

    private void logProcessingEvent(Event inputBeanDocument, String domainId) {
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug(String.format("Processing message from Response Detector using domain %s: %s", domainId,
                        inputBeanDocument.getId()));
                LOGGER.debug(String.format("Message: %s", inputBeanDocument));
            } catch (Exception e) {
            }
        }
    }

    public void setDetectionMessageProcessor(DetectionMessageProcessor detectionMessageProcessor) {
        this.detectionMessageProcessor = detectionMessageProcessor;
    }

    public void setExecutionMessageProcessor(ExecutionMessageProcessor executionMessageProcessor) {
        this.executionMessageProcessor = executionMessageProcessor;
    }
}
