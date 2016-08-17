package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocial.eventresolver.model.event.InPutBeanDocument;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.DomainService;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.factories.ElasticConfiguratorFactory;
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

    public void process(Message message) throws Exception {
        InPutBeanDocument inputBeanDocument = new GsonBuilder().setDateFormat(appConfig.getDateFormat()).create()
                .fromJson(message.getMessage(), InPutBeanDocument.class);
        log.info(String.format("Processing message for topic %s: %s", message.getTopic(), inputBeanDocument));
        String domainName = resolveDomain(message);
        log.info(String.format("Searching for domain: %s", domainName));
        Configurator elasticConfigConfigurator = elasticConfigConfiguratorFactory.getConfigurator(appConfig
                .getElasticConfigConfiguratorZnodePath());
        Domain domain = domainElasticService.findDomainByName(elasticConfigConfigurator, domainName);

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(inputBeanDocument, domain);
        if (detectionCriteria != null) {
            executionMessageProcessor.execute(inputBeanDocument, detectionCriteria);
        } else {
            log.info(String.format("Message did not detected to execute an action: %s", inputBeanDocument));
        }
    }

    private String resolveDomain(Message message) {
        return message.getTopic().split("\\.", 2)[1];
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
