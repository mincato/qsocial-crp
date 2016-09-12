package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.exception.InvalidDomainException;
import com.qsocialnow.eventresolver.filters.MessageFilter;
import com.qsocialnow.eventresolver.service.DomainService;
import com.qsocialnow.kafka.model.Message;

@Service
public class MessageProcessor {

    private Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private DetectionMessageProcessor detectionMessageProcessor;

    @Autowired
    private ExecutionMessageProcessor executionMessageProcessor;

    @Autowired
    private DomainService domainService;

    @Autowired
    private MessageFilter messageFilter;

    public void process(Message message) throws Exception {
        // reintentar ES
        InPutBeanDocument inputBeanDocument = new GsonBuilder().setDateFormat(appConfig.getDateFormat()).create()
                .fromJson(message.getMessage(), InPutBeanDocument.class);
        String domainId = message.getGroup();
        log.info(String.format("Processing message for domain %s: %s", domainId, inputBeanDocument));
        log.info(String.format("Searching for domain: %s", domainId));

        Domain domain = domainService.findDomainWithTriggers(domainId);
        if (domain != null) {
            if (messageFilter.shouldProcess(inputBeanDocument, domain)) {
                DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(inputBeanDocument, domain);
                if (detectionCriteria != null) {
                    ExecutionMessageRequest request = new ExecutionMessageRequest(inputBeanDocument, domain,
                            detectionCriteria);
                    executionMessageProcessor.execute(request);
                } else {
                    log.info(String.format("Message were not detected to execute an action: %s", inputBeanDocument));
                }
            } else {
                log.info(String.format("Message should not be processed for this domain: %s", domainId));
            }
        } else {
            throw new InvalidDomainException("Error trying to retrieve a domain");
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
