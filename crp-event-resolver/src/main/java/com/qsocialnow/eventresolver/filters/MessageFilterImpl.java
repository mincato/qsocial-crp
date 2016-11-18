package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.Event;

@Service
@Qualifier("messageFilter")
public class MessageFilterImpl implements MessageFilter {
	
    @Override
    public boolean shouldProcess(Event message, Domain domain) {
        boolean shouldProcess = false;

        if (domain != null && CollectionUtils.isNotEmpty(domain.getThematics())) {
        	System.out.println("=========> " + domain.getThematics());
        	System.out.println("=========> " + message.getTokenId());
            shouldProcess = domain.getThematics().stream().anyMatch(thematic -> thematic.equals(message.getTokenId()));
        }

        return shouldProcess;
    }

}
