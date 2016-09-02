package com.qsocialnow.eventresolver.filters;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("mediaDetectionCriteriaFilter")
public class MediaDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(MediaDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing media message filter");
        boolean match = false;
        if (message.getMedioId() != null) {
            Long[] medias = filter.getMediaFilter().getOptions();
            match = ArrayUtils.contains(medias, message.getMedioId());
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getMediaFilter() != null && ArrayUtils.isNotEmpty(filter.getMediaFilter().getOptions());
    }

}
