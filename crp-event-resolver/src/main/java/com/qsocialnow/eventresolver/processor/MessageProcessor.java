package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.config.DomainService;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.factories.ElasticConfiguratorFactory;
import com.qsocialnow.eventresolver.filters.MessageFilter;
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
    private DomainService domainElasticService;

    @Autowired
    private ElasticConfiguratorFactory elasticConfigConfiguratorFactory;

    @Autowired
    private MessageFilter messageFilter;

    public void process(Message message) throws Exception {
        InPutBeanDocument inputBeanDocument = new GsonBuilder().setDateFormat(appConfig.getDateFormat()).create()
                .fromJson(message.getMessage(), InPutBeanDocument.class);
        String domainId = message.getGroup();
        log.info(String.format("Processing message for domain %s: %s", domainId, inputBeanDocument));
        log.info(String.format("Searching for domain: %s", domainId));
        Configurator elasticConfigConfigurator = elasticConfigConfiguratorFactory.getConfigurator(appConfig
                .getElasticConfigConfiguratorZnodePath());
        Domain domain = domainElasticService.findDomain(elasticConfigConfigurator, domainId);
        if (messageFilter.shouldProcess(inputBeanDocument, domain)) {
            DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(inputBeanDocument, domain);
            if (detectionCriteria != null) {
                executionMessageProcessor.execute(inputBeanDocument, detectionCriteria);
            } else {
                log.info(String.format("Message were not detected to execute an action: %s", inputBeanDocument));
            }
        } else {
            log.info(String.format("Message should not be processed for this domain: %s", domainId));
        }
    }

    public void setDomainElasticService(DomainService domainElasticService) {
        this.domainElasticService = domainElasticService;
    }

    public void setDetectionMessageProcessor(DetectionMessageProcessor detectionMessageProcessor) {
        this.detectionMessageProcessor = detectionMessageProcessor;
    }

    public void setExecutionMessageProcessor(ExecutionMessageProcessor executionMessageProcessor) {
        this.executionMessageProcessor = executionMessageProcessor;
    }
}
