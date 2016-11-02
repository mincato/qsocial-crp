package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.CategoryFilter;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.util.ObjectUtils;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("categoryDetectionCriteriaFilter")
public class CategoryDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(CategoryDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.debug("Executing category message filter");
        boolean match = false;
        if (message.getConjuntos() != null) {
            match = filter.getCategoryFilter().stream()
                    .anyMatch(categoryFilter -> messageContainsGroupAndCategories(message, categoryFilter));
        }
        return match;
    }

    private boolean messageContainsGroupAndCategories(NormalizedInputBeanDocument message, CategoryFilter categoryFilter) {
        return ArrayUtils.contains(message.getConjuntos(), categoryFilter.getCategoryGroup())
                && (ArrayUtils.isEmpty(categoryFilter.getCategories()) || ObjectUtils.containsAll(
                        message.getCategorias(), categoryFilter.getCategories()));
    }

    @Override
    public boolean apply(Filter filter) {
        return CollectionUtils.isNotEmpty(filter.getCategoryFilter())
                && filter.getCategoryFilter().stream()
                        .anyMatch(categoryFilter -> categoryFilter.getCategoryGroup() != null);
    }

}
