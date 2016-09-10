package com.qsocialnow.eventresolver.processor;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.InPutBeanDocument;

public class ExecutionMessageRequest {

	private final InPutBeanDocument input;

	private final DetectionCriteria detectionCriteria;

	private final Domain domain;

	public ExecutionMessageRequest(final InPutBeanDocument input, final Domain domain,
			final DetectionCriteria detectionCriteria) {
		this.input = input;
		this.domain = domain;
		this.detectionCriteria = detectionCriteria;
	}

	public InPutBeanDocument getInput() {
		return input;
	}
	
	public DetectionCriteria getDetectionCriteria() {
		return detectionCriteria;
	}


	public Domain getDomain() {
		return domain;
	}
}
