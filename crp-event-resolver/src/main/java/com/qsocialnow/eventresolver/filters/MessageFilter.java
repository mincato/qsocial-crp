package com.qsocialnow.eventresolver.filters;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.Event;

public interface MessageFilter {

    boolean shouldProcess(Event message, Domain domain);

}
