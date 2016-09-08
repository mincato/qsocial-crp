package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.util.ObjectUtils;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("categoryDetectionCriteriaFilter")
public class CategoryDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(CategoryDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing category message filter");
        boolean match = false;
        if (message.getCategorias() != null) {
            match = filter
                    .getCategoryFilter()
                    .stream()
                    .anyMatch(
                            categoryFilter -> ObjectUtils.containsAll(message.getCategorias(),
                                    categoryFilter.getCategories()));
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return CollectionUtils.isNotEmpty(filter.getCategoryFilter())
                && filter.getCategoryFilter().stream()
                        .anyMatch(categoryFilter -> ArrayUtils.isNotEmpty(categoryFilter.getCategories()));
    }

}
