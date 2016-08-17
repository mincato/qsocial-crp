package com.qsocial.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocial.eventresolver.model.event.InPutBeanDocument;

public class FalseMessageFilter implements MessageFilter {

    private static final Logger log = LoggerFactory.getLogger(FalseMessageFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing false message filter");
        return false;
    }

}
