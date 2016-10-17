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

	public void process(Message message) throws Exception {
		// reintentar ES
		Event inputBeanDocument = new GsonBuilder().create().fromJson(message.getMessage(), Event.class);
		String domainId = message.getGroup();
		logProcessingEvent(inputBeanDocument, domainId);

		Domain domain = domainService.findDomainWithActiveTriggers(domainId);
		if (domain != null) {
			if (messageFilter.shouldProcess(inputBeanDocument, domain)) {
				ExecutionMessageRequest executionMessageRequest = detectionMessageProcessor.detect(inputBeanDocument,
						domain);
				if (executionMessageRequest != null) {
					executionMessageProcessor.execute(executionMessageRequest);
				} else {
					logMessageNotDetected(inputBeanDocument);
				}
			} else {
				LOGGER.info(String.format("Message should not be processed for this domain: %s", domainId));
			}
		} else {
			throw new InvalidDomainException("Error trying to retrieve a domain");
		}
	}

	private void logMessageNotDetected(Event inputBeanDocument) {
		LOGGER.info(String.format("Message were not detected to execute an action: %s",
				inputBeanDocument.getId()));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Message: %s", inputBeanDocument));
		}
	}

	private void logProcessingEvent(Event inputBeanDocument, String domainId) {
		LOGGER.info(String.format("Processing message for domain %s: %s", domainId, inputBeanDocument.getId()));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Message: %s", inputBeanDocument));
		}
		LOGGER.info(String.format("Searching for domain: %s", domainId));
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
