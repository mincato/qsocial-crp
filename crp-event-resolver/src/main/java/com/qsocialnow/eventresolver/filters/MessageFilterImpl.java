package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.InPutBeanDocument;

@Service
public class MessageFilterImpl implements MessageFilter {

    @Override
    public boolean shouldProcess(InPutBeanDocument message, Domain domain) {
        boolean shouldProcess = false;

        if (message!=null && message.isResponseDetected()){
        	return true;
        }

        if (domain != null && CollectionUtils.isNotEmpty(domain.getThematics())) {
            shouldProcess = domain.getThematics().stream().anyMatch(thematic -> thematic.equals(message.getTokenId()));
        }

        return shouldProcess;
    }

}
