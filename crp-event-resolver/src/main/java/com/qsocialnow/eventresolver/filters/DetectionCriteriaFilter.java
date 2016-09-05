package com.qsocialnow.eventresolver.filters;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public interface DetectionCriteriaFilter {

    public boolean match(final NormalizedInputBeanDocument normalizedMessage, final Filter filter);

    public boolean apply(final Filter filter);

}
