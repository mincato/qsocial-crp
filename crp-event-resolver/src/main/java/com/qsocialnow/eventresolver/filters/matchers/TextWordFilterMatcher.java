package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;
import com.qsocialnow.eventresolver.normalizer.TextNormalizer;

@Component("textWordFilterMatcher")
public class TextWordFilterMatcher implements WordFilterMatcher {

    @Autowired
    private TextNormalizer textNormalizer;

    @Override
    public boolean match(NormalizedInputBeanDocument message, Set<String> filterTexts) {
        boolean match = false;
        if (message.getTexto() != null) {
            Set<String> messageText = message.getNormalizedTexto(textNormalizer);
            match = CollectionUtils.containsAll(messageText, filterTexts);
        }

        return match;
    }

}
