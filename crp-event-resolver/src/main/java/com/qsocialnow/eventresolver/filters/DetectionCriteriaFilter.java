package com.qsocialnow.eventresolver.filters;

import com.qsocialnow.common.model.event.InPutBeanDocument;

public interface DetectionCriteriaFilter {

    public boolean match(InPutBeanDocument message, String attributeValues);

}
