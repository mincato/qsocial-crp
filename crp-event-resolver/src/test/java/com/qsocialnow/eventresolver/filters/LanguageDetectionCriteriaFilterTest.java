package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.LanguageFilter;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class LanguageDetectionCriteriaFilterTest {

    private LanguageDetectionCriteriaFilter filter;
    private Filter filterConfig;

    public LanguageDetectionCriteriaFilterTest() {
        filter = new LanguageDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "1" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullLanguageFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullOptions() {
        LanguageFilter languageFilter = new LanguageFilter();
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseEmptyOptions() {
        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[0]);
        filterConfig.setLanguageFilter(languageFilter);
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        InPutBeanDocument input = new InPutBeanDocument();

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "es" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setLanguage("es");

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "es" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setLanguage("es");

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "pt", "es" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setLanguage("es");

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "pt" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setLanguage("es");

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "en", "pt" });
        filterConfig.setLanguageFilter(languageFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
