package com.qsocialnow.eventresolver.filters;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("connotationDetectionCriteriaFilter")
public class ConnotationDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(ConnotationDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing connotation message filter");
        boolean match = false;
        if (message.getConnotacion() != null) {
            Short[] connotationOptions = filter.getConnotationFilter().getOptions();
            match = ArrayUtils.contains(connotationOptions, message.getConnotacion());
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getConnotationFilter() != null
                && ArrayUtils.isNotEmpty(filter.getConnotationFilter().getOptions());
    }

}
