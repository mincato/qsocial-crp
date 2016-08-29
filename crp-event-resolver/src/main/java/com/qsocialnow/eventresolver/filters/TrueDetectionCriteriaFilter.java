package com.qsocialnow.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Component("trueDetectionCriteriaFilter")
public class TrueDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(TrueDetectionCriteriaFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing true message filter");
        return true;
    }

}
