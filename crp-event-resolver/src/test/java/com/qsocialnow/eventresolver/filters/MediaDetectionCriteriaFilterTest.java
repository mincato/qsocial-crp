package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class MediaDetectionCriteriaFilterTest {

    private MediaDetectionCriteriaFilter filter;
    private Filter filterConfig;

    public MediaDetectionCriteriaFilterTest() {
        filter = new MediaDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 1l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullMediaFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullOptions() {
        MediaFilter mediaFilter = new MediaFilter();
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseEmptyOptions() {
        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[0]);
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        InPutBeanDocument input = new InPutBeanDocument();

        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 1l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 1l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 1l, 2l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));

    }

    @Test
    public void testMatchFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 2l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 2l, 3l });
        filterConfig.setMediaFilter(mediaFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
