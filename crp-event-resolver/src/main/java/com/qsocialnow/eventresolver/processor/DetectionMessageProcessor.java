package com.qsocialnow.eventresolver.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Service
public class DetectionMessageProcessor {

    @Autowired
    private DetectionCriteriaResolver detectionCriteriaResolver;

    public ExecutionMessageRequest detect(Event message, Domain domain) {
        ExecutionMessageRequest executionMessageRequest = null;
        DetectionCriteria detectionCriteria = null;

        // validate response detected
        if (message != null && message.isResponseDetected()) {
            detectionCriteria = new DetectionCriteria();
            return new ExecutionMessageRequest(message, domain, detectionCriteria, null, null);
        }

        boolean found = false;
        Trigger triggerDetected = null;
        Segment segmentDetected = null;
        if (domain != null && domain.getTriggers() != null) {
            NormalizedInputBeanDocument normalizedMessage = new NormalizedInputBeanDocument(message);
            for (int i = 0; !found && i < domain.getTriggers().size(); i++) {
                Trigger trigger = domain.getTriggers().get(i);
                if (trigger.getSegments() != null) {
                    for (int j = 0; !found && j < trigger.getSegments().size(); j++) {
                        Segment segment = trigger.getSegments().get(j);
                        detectionCriteria = detectionCriteriaResolver.resolve(normalizedMessage,
                                segment.getDetectionCriterias());
                        found = detectionCriteria != null;
                        if (found) {
                            segmentDetected = segment;
                        }
                    }
                }
                if (found) {
                    triggerDetected = trigger;
                }
            }
        }
        if (found) {
            executionMessageRequest = new ExecutionMessageRequest(message, domain, detectionCriteria, triggerDetected,
                    segmentDetected);
        }
        return executionMessageRequest;
    }

    public void setDetectionCriteriaResolver(DetectionCriteriaResolver detectionCriteriaResolver) {
        this.detectionCriteriaResolver = detectionCriteriaResolver;
    }

}
