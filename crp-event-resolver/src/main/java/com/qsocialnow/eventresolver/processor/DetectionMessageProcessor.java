package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(DetectionMessageProcessor.class);

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
                if (trigger.getSegments() != null && isTriggerValidRightNow(trigger)) {
                    for (int j = 0; !found && j < trigger.getSegments().size(); j++) {
                        Segment segment = trigger.getSegments().get(j);
                        detectionCriteria = detectionCriteriaResolver.resolve(normalizedMessage,
                                segment.getDetectionCriterias());
                        found = detectionCriteria != null;
                        if (found) {
                            segmentDetected = segment;
                        }
                    }
                } else {
                    log.debug(String.format("The trigger with name %s is not valid this time", trigger.getName()));
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

    private boolean isTriggerValidRightNow(Trigger trigger) {
        long currentTime = System.currentTimeMillis();
        boolean isValid = ((trigger.getInit() == null || trigger.getInit() <= currentTime) && (trigger.getEnd() == null || currentTime <= trigger
                .getEnd()));
        return isValid;
    }

    public void setDetectionCriteriaResolver(DetectionCriteriaResolver detectionCriteriaResolver) {
        this.detectionCriteriaResolver = detectionCriteriaResolver;
    }

}
