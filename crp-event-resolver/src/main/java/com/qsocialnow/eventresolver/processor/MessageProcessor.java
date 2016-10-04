package com.qsocialnow.eventresolver.processor;

import com.qsocialnow.eventresolver.service.DomainService;
import com.qsocialnow.kafka.model.Message;

public interface MessageProcessor {

    public void process(Message message) throws Exception;
    
    public void setDomainElasticService(DomainService domainElasticService);

    public void setDetectionMessageProcessor(DetectionMessageProcessor detectionMessageProcessor);

    public void setExecutionMessageProcessor(ExecutionMessageProcessor executionMessageProcessor) ;
}
