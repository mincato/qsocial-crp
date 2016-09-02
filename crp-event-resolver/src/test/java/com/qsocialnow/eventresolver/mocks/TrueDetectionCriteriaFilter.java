package com.qsocialnow.eventresolver.mocks;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.filters.DetectionCriteriaFilter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("trueDetectionCriteriaFilter")
public class TrueDetectionCriteriaFilter implements DetectionCriteriaFilter {

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        return true;
    }

    @Override
    public boolean apply(Filter filter) {
        return "true".equals(filter.getId());
    }

}
