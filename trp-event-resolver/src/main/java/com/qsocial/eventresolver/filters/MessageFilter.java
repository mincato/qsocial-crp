package com.qsocial.eventresolver.filters;

import com.qsocial.eventresolver.model.event.InPutBeanDocument;

public interface MessageFilter {

    public boolean match(InPutBeanDocument message, String attributeValues);

}
