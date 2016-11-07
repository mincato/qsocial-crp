package com.qsocialnow.eventresolver.filters;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.eventresolver.filters.matchers.WordFilterMatcher;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("wordUnitDetectionCriteriaFilter")
public class WordDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(WordDetectionCriteriaFilter.class);

    @Resource
    private Map<WordFilterType, WordFilterMatcher> matchers;

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.debug("Executing word message filter");
        boolean match = false;
        if (messageHasWords(message)) {
            match = filter.getWordFilters().stream()
                    .anyMatch(wordFilter -> matchers.get(wordFilter.getType()).match(message, wordFilter.getText()));
        }
        return match;
    }

    private boolean messageHasWords(NormalizedInputBeanDocument message) {
        return message.getTexto() != null || message.getActores() != null || message.getHashTags() != null
                || message.getUsuarioCreacion() != null || message.getUsuarioReproduccion() != null;
    }

    @Override
    public boolean apply(Filter filter) {
        return CollectionUtils.isNotEmpty(filter.getWordFilters());
    }

}
