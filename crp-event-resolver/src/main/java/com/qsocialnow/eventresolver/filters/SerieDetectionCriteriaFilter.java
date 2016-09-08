package com.qsocialnow.eventresolver.filters;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("serieDetectionCriteriaFilter")
public class SerieDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(SerieDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing serie message filter");
        boolean match = false;
        if (message.getTokenId() != null) {
            match = message.getTokenId().equals(filter.getSerieFilter().getThematicId());
            if (match && filter.getSerieFilter().getSerieId() != null) {
                match = ArrayUtils.contains(message.getSerieIds(), filter.getSerieFilter().getSerieId());
                if (match && filter.getSerieFilter().getSubSerieId() != null) {
                    match = ArrayUtils.contains(message.getSubSerieIds(), filter.getSerieFilter().getSerieId());
                }
            }
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getSerieFilter() != null && filter.getSerieFilter().getThematicId() != null;
    }

}
