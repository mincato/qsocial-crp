package com.qsocialnow.eventresolver.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.filters.matchers.WordFilterMatcher;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@RunWith(MockitoJUnitRunner.class)
public class WordDetectionCriteriaFilterTest {

    @InjectMocks
    private WordDetectionCriteriaFilter filter;

    private Filter filterConfig;

    @Mock
    private Map<WordFilterType, WordFilterMatcher> matchers;

    public WordDetectionCriteriaFilterTest() {
        filter = new WordDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter));

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullWordFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseEmptyWordFilters() {
        filterConfig.setWordFilters(new ArrayList<>());

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        InPutBeanDocument input = new InPutBeanDocument();

        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueOneFilter() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setActores(new String[] { "actor1" });

        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter));

        WordFilterMatcher matcher = Mockito.mock(WordFilterMatcher.class);

        Mockito.when(matchers.get(WordFilterType.AUTHOR)).thenReturn(matcher);
        Mockito.when(matcher.match(Mockito.any(NormalizedInputBeanDocument.class), Mockito.anySetOf(String.class)))
                .thenReturn(true);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueTwoFilterFirstFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setActores(new String[] { "actor1" });

        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        WordFilter wordFilter1 = new WordFilter();
        wordFilter1.setType(WordFilterType.MENTION);
        wordFilter1.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter, wordFilter1));

        WordFilterMatcher matcherFalse = Mockito.mock(WordFilterMatcher.class);
        WordFilterMatcher matcherTrue = Mockito.mock(WordFilterMatcher.class);

        Mockito.when(matchers.get(WordFilterType.AUTHOR)).thenReturn(matcherFalse);
        Mockito.when(matcherFalse.match(Mockito.any(NormalizedInputBeanDocument.class), Mockito.anySetOf(String.class)))
                .thenReturn(false);

        Mockito.when(matchers.get(WordFilterType.MENTION)).thenReturn(matcherTrue);
        Mockito.when(matcherTrue.match(Mockito.any(NormalizedInputBeanDocument.class), Mockito.anySetOf(String.class)))
                .thenReturn(true);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseOneFilter() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setActores(new String[] { "actor1" });

        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter));

        WordFilterMatcher matcher = Mockito.mock(WordFilterMatcher.class);

        Mockito.when(matchers.get(WordFilterType.AUTHOR)).thenReturn(matcher);
        Mockito.when(matcher.match(Mockito.any(NormalizedInputBeanDocument.class), Mockito.anySetOf(String.class)))
                .thenReturn(false);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseTwoFilters() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setActores(new String[] { "actor1" });

        WordFilter wordFilter = new WordFilter();
        wordFilter.setType(WordFilterType.AUTHOR);
        wordFilter.setText(new HashSet<>());
        WordFilter wordFilter1 = new WordFilter();
        wordFilter1.setType(WordFilterType.MENTION);
        wordFilter1.setText(new HashSet<>());
        filterConfig.setWordFilters(Arrays.asList(wordFilter, wordFilter1));

        WordFilterMatcher matcherFalse = Mockito.mock(WordFilterMatcher.class);

        Mockito.when(matchers.get(WordFilterType.AUTHOR)).thenReturn(matcherFalse);
        Mockito.when(matchers.get(WordFilterType.MENTION)).thenReturn(matcherFalse);
        Mockito.when(matcherFalse.match(Mockito.any(NormalizedInputBeanDocument.class), Mockito.anySetOf(String.class)))
                .thenReturn(false);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
