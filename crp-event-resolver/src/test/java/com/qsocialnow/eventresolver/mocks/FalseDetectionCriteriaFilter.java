package com.qsocialnow.eventresolver.mocks;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.filters.DetectionCriteriaFilter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("falseDetectionCriteriaFilter")
public class FalseDetectionCriteriaFilter implements DetectionCriteriaFilter {

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        return false;
    }

    @Override
    public boolean apply(Filter filter) {
        return "false".equals(filter.getId());
    }

}
