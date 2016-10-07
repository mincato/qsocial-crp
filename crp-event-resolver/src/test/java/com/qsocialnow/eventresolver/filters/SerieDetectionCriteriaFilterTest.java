package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.SerieFilter;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class SerieDetectionCriteriaFilterTest {

    private SerieDetectionCriteriaFilter filter;

    private Filter filterConfig;

    public SerieDetectionCriteriaFilterTest() {
        filter = new SerieDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullSerieFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullThematic() {
        SerieFilter serieFilter = new SerieFilter();
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        Event input = new Event();

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueOnlyThematic() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueThematicAndSerie() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        serieFilter.setSerieId(3l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueThematicAndSerieAndSubserie() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        serieFilter.setSerieId(3l);
        serieFilter.setSubSerieId(4l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseOnlyThematic() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(2l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseThematicAndSerieWrongThematic() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(2l);
        serieFilter.setSerieId(2l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseThematicAndSerieWrongSerie() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        serieFilter.setSerieId(4l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseThematicAndSerieAndSubserieWrongSerie() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        serieFilter.setSerieId(4l);
        serieFilter.setSubSerieId(4l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseThematicAndSerieAndSubserieWrongSubSerie() {
        Event input = new Event();
        input.setTokenId(1l);
        input.setSeriesId(new Long[] { 2l, 3l });
        input.setSubSeriesId(new Long[] { 4l, 5l });

        SerieFilter serieFilter = new SerieFilter();
        serieFilter.setThematicId(1l);
        serieFilter.setSerieId(2l);
        serieFilter.setSubSerieId(6l);
        filterConfig.setSerieFilter(serieFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
