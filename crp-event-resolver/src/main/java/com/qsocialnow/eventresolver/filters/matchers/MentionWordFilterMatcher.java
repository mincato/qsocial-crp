package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("mentionWordFilterMatcher")
public class MentionWordFilterMatcher implements WordFilterMatcher {

    @Override
    public boolean match(NormalizedInputBeanDocument message, Set<String> filterMentions) {
        boolean match = false;
        if (ArrayUtils.isNotEmpty(message.getActores())) {
            Set<String> messageMentions = message.getNormalizedActores();
            match = CollectionUtils.containsAll(messageMentions, filterMentions);
        }

        return match;
    }

}
