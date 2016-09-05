package com.qsocialnow.eventresolver.filters;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("languageDetectionCriteriaFilter")
public class LanguageDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(LanguageDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing language message filter");
        boolean match = false;
        if (message.getLanguage() != null) {
            String[] languages = filter.getLanguageFilter().getOptions();
            match = ArrayUtils.contains(languages, message.getLanguage());
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getLanguageFilter() != null && ArrayUtils.isNotEmpty(filter.getLanguageFilter().getOptions());
    }

}
