package com.qsocial.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocial.eventresolver.model.event.InPutBeanDocument;

public class TrueMessageFilter implements MessageFilter {

    private static final Logger log = LoggerFactory.getLogger(TrueMessageFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing true message filter");
        return true;
    }

}
