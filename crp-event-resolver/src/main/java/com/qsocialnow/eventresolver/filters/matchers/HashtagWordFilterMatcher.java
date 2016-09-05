package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("hashtagWordFilterMatcher")
public class HashtagWordFilterMatcher implements WordFilterMatcher {

    @Override
    public boolean match(NormalizedInputBeanDocument message, Set<String> filterHashTags) {
        boolean match = false;
        if (ArrayUtils.isNotEmpty(message.getHashTags())) {
            Set<String> messageHashTags = message.getNormalizedHashTags();
            match = CollectionUtils.containsAll(messageHashTags, filterHashTags);
        }

        return match;
    }

}
