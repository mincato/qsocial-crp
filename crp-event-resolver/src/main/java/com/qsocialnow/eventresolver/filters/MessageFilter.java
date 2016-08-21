package com.qsocialnow.eventresolver.filters;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

public interface MessageFilter {

    boolean shouldProcess(InPutBeanDocument message, Domain domain);

}
