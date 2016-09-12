package com.qsocialnow.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("periodDetectionCriteriaFilter")
public class PeriodDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(PeriodDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing period message filter");
        boolean match = false;
        if (message.getTimestamp() != null) {
            Long startDate = filter.getPeriodFilter().getStartDateTime();
            Long endDate = filter.getPeriodFilter().getEndDateTime();
            Long messageDate = message.getTimestamp();
            if (startDate != null && endDate != null) {
                match = startDate <= messageDate && messageDate <= endDate;
            } else if (startDate != null) {
                match = startDate <= messageDate;
            } else if (endDate != null) {
                match = messageDate <= endDate;
            }
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getPeriodFilter() != null
                && (filter.getPeriodFilter().getStartDateTime() != null || filter.getPeriodFilter().getEndDateTime() != null);
    }

}
