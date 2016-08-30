package com.qsocialnow.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.event.InPutBeanDocument;

@Component("falseDetectionCriteriaFilter")
public class FalseDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(FalseDetectionCriteriaFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing false message filter");
        return false;
    }

}
