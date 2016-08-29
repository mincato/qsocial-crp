package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Service
public class MessageFilterImpl implements MessageFilter {

    @Override
    public boolean shouldProcess(InPutBeanDocument message, Domain domain) {
        boolean shouldProcess = false;
        if (domain != null && CollectionUtils.isNotEmpty(domain.getThematics())) {
            shouldProcess = domain.getThematics().stream().anyMatch(thematic -> thematic.equals(message.getTokenId()));
        }
        return shouldProcess;
    }

}