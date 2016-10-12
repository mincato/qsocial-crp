package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.ConnotationFilter;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class ConnotationDetectionCriteriaFilterTest {

    private ConnotationDetectionCriteriaFilter filter;

    private Filter filterConfig;

    public ConnotationDetectionCriteriaFilterTest() {
        filter = new ConnotationDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 1 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullConnotationFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullOptions() {
        ConnotationFilter connotationFilter = new ConnotationFilter();
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseEmptyOptions() {
        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[0]);
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        Event input = new Event();

        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 1 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrue() {
        Event input = new Event();
        input.setConnotacion((short) 1);

        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 1 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        Event input = new Event();
        input.setConnotacion((short) 1);

        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 1, 2 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalse() {
        Event input = new Event();
        input.setConnotacion((short) 1);

        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 2 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        Event input = new Event();
        input.setConnotacion((short) 1);

        ConnotationFilter connotationFilter = new ConnotationFilter();
        connotationFilter.setOptions(new Short[] { 2, 3 });
        filterConfig.setConnotationFilter(connotationFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
