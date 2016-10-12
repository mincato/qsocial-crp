package com.qsocialnow.eventresolver.filters;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.Event;

@Service
@Qualifier("messageResponseDetectorFilter")
public class MessageResponseDetectorFilterImpl implements MessageFilter {

    @Override
    public boolean shouldProcess(Event message, Domain domain) {
        boolean shouldProcess = false;

        if (message != null && message.isResponseDetected()) {
            return true;
        }

        return shouldProcess;
    }

}
