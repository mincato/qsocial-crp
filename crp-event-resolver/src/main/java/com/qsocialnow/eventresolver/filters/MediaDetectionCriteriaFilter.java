package com.qsocialnow.eventresolver.filters;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Component("mediaDetectionCriteriaFilter")
public class MediaDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(MediaDetectionCriteriaFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing media message filter");
        boolean match = false;
        if (attributeValues != null) {
            String[] medias = attributeValues.split("\\|");
            match = ArrayUtils.contains(medias, String.valueOf(message.getMedioId()));
        }
        return match;
    }

}
