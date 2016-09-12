package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Set;

import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public interface WordFilterMatcher {

    boolean match(NormalizedInputBeanDocument message, Set<String> filterTexts);

}
