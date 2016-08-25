package com.qsocialnow.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Component("connotationDetectionCriteriaFilter")
public class ConnotationDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(ConnotationDetectionCriteriaFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing connotation message filter");
        boolean match = false;
        if (attributeValues != null) {
            match = message.getConnotacion().equals(Short.parseShort(attributeValues));
        }
        return match;
    }

}
