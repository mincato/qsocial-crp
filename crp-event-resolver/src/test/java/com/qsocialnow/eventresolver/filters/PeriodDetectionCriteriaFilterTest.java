package com.qsocialnow.eventresolver.filters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.PeriodFilter;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class PeriodDetectionCriteriaFilterTest {

    private PeriodDetectionCriteriaFilter filter;

    private DateFormat sdf = new SimpleDateFormat(FilterConstants.DATE_TIME_FORMAT);

    private Filter filterConfig;

    public PeriodDetectionCriteriaFilterTest() {
        filter = new PeriodDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrueBoth() throws Exception {
        PeriodFilter periodFilter = new PeriodFilter();

        String startDate = "10/07/2016-15:00";
        String endDate = "10/09/2016-15:00";

        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyTrueStartDate() throws Exception {
        PeriodFilter periodFilter = new PeriodFilter();

        String startDate = "10/07/2016-15:00";

        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyTrueEndDate() throws Exception {
        PeriodFilter periodFilter = new PeriodFilter();

        String endDate = "10/09/2016-15:00";

        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullPeriodFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullStartAndEndDates() {
        PeriodFilter periodFilter = new PeriodFilter();
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() throws Exception {
        Event input = new Event();

        PeriodFilter periodFilter = new PeriodFilter();

        String startDate = "10/07/2016-15:00";
        String endDate = "10/09/2016-15:00";

        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueBetweenRange() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/07/2016-15:00";
        String endDate = "10/09/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueBetweenRangeHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-14:00";
        String endDate = "10/08/2016-16:00";
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueBetweenRangeEquals() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-15:00";
        String endDate = "10/08/2016-15:00";
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueFromDate() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/07/2016-15:00";
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueFromDateHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-14:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueFromDateEquals() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));

    }

    @Test
    public void testMatchTrueUntilDate() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String endDate = "10/09/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));

    }

    @Test
    public void testMatchTrueUntilDateHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String endDate = "10/08/2016-16:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));

    }

    @Test
    public void testMatchTrueUntilDateEquals() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String endDate = "10/08/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));

    }

    @Test
    public void testMatchFalseBetweenRange() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/05/2016-15:00";
        String endDate = "10/07/2016-15:00";
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseBetweenRangeHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-12:00";
        String endDate = "10/08/2016-14:00";
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseFromDate() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/09/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseFromDateHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String startDate = "10/08/2016-16:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setStartDateTime(sdf.parse(startDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseUntilDate() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String endDate = "10/07/2016-15:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseUntilDateHours() throws Exception {
        Event input = new Event();
        String date = "10/08/2016-15:00";
        input.setTimestamp(sdf.parse(date).getTime());

        String endDate = "10/08/2016-14:00";

        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setEndDateTime(sdf.parse(endDate).getTime());
        filterConfig.setPeriodFilter(periodFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
