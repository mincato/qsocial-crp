package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.eventresolver.filters.MessageFilter;
import com.qsocialnow.eventresolver.service.DomainService;
import com.qsocialnow.eventresolver.service.UserResolverService;
import com.qsocialnow.kafka.model.Message;

@Service
public class MessageProcessorImpl implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorImpl.class);

    @Autowired
    private DetectionMessageProcessor detectionMessageProcessor;

    @Autowired
    private ExecutionMessageProcessor executionMessageProcessor;

    @Autowired
    private DomainService domainService;

    @Autowired
    @Qualifier("messageFilter")
    private MessageFilter messageFilter;

    @Autowired
    private UserResolverService userResolverService;

    public void process(Message message) throws Exception {
        // reintentar ES
        Event inputBeanDocument = new GsonBuilder().create().fromJson(message.getMessage(), Event.class);
        if (inputBeanDocument == null) {
            return;
        }

        String domainId = message.getGroup();
        logProcessingEvent(inputBeanDocument, domainId);

        Domain domain = domainService.findDomainWithActiveTriggers(domainId);
        if (domain != null) {
            if (messageFilter.shouldProcess(inputBeanDocument, domain)) {
                if (!isUserResolverEvent(inputBeanDocument)) {
                    ExecutionMessageRequest executionMessageRequest = detectionMessageProcessor.detect(
                            inputBeanDocument, domain);
                    if (executionMessageRequest != null) {
                        executionMessageProcessor.execute(executionMessageRequest);
                    } else {
                        logMessageNotDetected(inputBeanDocument);
                    }
                }
            } else {
                LOGGER.debug(String.format("Message should not be processed for this domain: %s", domainId));
            }
        } else {
            throw new InvalidDomainException("Error trying to retrieve a domain");
        }
    }

    private boolean isUserResolverEvent(Event inputBeanDocument) {
        boolean isUserResolverEvent = false;
        if (userResolverService.findAllSourceIds().contains(inputBeanDocument.getIdUsuarioCreacion())) {
            LOGGER.debug("The event is from user resolver. So, it is discarded");
            isUserResolverEvent = true;
        }
        return isUserResolverEvent;
    }

    private void logMessageNotDetected(Event inputBeanDocument) {
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug(String.format("Message were not detected to execute an action: %s",
                        inputBeanDocument.getId()));
                LOGGER.debug(String.format("Message: %s", inputBeanDocument));
            } catch (Exception e) {
            }
        }
    }

    private void logProcessingEvent(Event inputBeanDocument, String domainId) {
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug(String.format("Processing message for domain %s: %s", domainId, inputBeanDocument.getId()));
                LOGGER.debug(String.format("Message: %s", inputBeanDocument));
                LOGGER.debug(String.format("Searching for domain: %s", domainId));
            } catch (Exception e) {
            }
        }
    }

    public void setDomainElasticService(DomainService domainElasticService) {
        this.domainService = domainElasticService;
    }

    public void setDetectionMessageProcessor(DetectionMessageProcessor detectionMessageProcessor) {
        this.detectionMessageProcessor = detectionMessageProcessor;
    }

    public void setExecutionMessageProcessor(ExecutionMessageProcessor executionMessageProcessor) {
        this.executionMessageProcessor = executionMessageProcessor;
    }
}
