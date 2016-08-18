package com.qsocialnow.eventresolver.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Service
public class DetectionMessageProcessor {

    @Autowired
    private DetectionCriteriaResolver detectionCriteriaResolver;

    public DetectionCriteria detect(InPutBeanDocument message, Domain domain) {
        DetectionCriteria detectionCriteria = null;
        boolean found = false;
        if (domain != null && domain.getTriggers() != null) {
            for (int i = 0; !found && i < domain.getTriggers().size(); i++) {
                Trigger trigger = domain.getTriggers().get(i);
                if (trigger.getSegments() != null) {
                    for (int j = 0; !found && j < trigger.getSegments().size(); j++) {
                        Segment segments = trigger.getSegments().get(j);
                        detectionCriteria = detectionCriteriaResolver
                                .resolve(message, segments.getDetectionCriterias());
                        found = detectionCriteria != null;
                    }
                }
            }
        }
        return detectionCriteria;
    }

    public void setDetectionCriteriaResolver(DetectionCriteriaResolver detectionCriteriaResolver) {
        this.detectionCriteriaResolver = detectionCriteriaResolver;
    }

}
