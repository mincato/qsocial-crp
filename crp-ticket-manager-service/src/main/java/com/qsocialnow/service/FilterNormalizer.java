package com.qsocialnow.service;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.common.util.StringUtils;

@Component
public class FilterNormalizer {

    public void normalizeFilter(Filter filter) {
        if (filter != null && filter.getWordFilters() != null) {
            filter.getWordFilters()
                    .stream()
                    .forEach(
                            wordFilter -> {
                                if (wordFilter.getText() != null) {
                                    switch (wordFilter.getType()) {
                                        case HASHTAG:
                                        case MENTION:
                                            wordFilter.setText(StringUtils.convert2LowerCase(wordFilter.getInputText()
                                                    .split(FilterConstants.COMMA_SEPARATOR)));
                                            break;
                                        case TEXT:
                                            wordFilter.setText(StringUtils.convertLowerCaseAsciiFolding(wordFilter
                                                    .getInputText().split(FilterConstants.COMMA_SEPARATOR)));
                                        default:
                                            break;
                                    }
                                }
                            });
        }
    }

}
