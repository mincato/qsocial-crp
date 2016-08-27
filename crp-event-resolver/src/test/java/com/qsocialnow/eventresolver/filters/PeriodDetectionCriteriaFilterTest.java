package com.qsocialnow.eventresolver.filters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

public class PeriodDetectionCriteriaFilterTest {

    private PeriodDetectionCriteriaFilter filter;

    private DateFormat sdf = new SimpleDateFormat(FilterConstants.DATE_TIME_FORMAT);

    public PeriodDetectionCriteriaFilterTest() {
        filter = new PeriodDetectionCriteriaFilter();
    }

    @Test
    public void testMatchParameterNull() {
        Assert.assertFalse(filter.match(null, null));
    }

    @Test
    public void testMatchTrueBetweenRange() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/07/2016-15:00|10/09/2016-15:00"));
    }

    @Test
    public void testMatchTrueBetweenRangeHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/08/2016-14:00|10/08/2016-16:00"));
    }

    @Test
    public void testMatchTrueBetweenRangeEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/08/2016-15:00|10/08/2016-15:00"));
    }

    @Test
    public void testMatchTrueFromDate() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/07/2016-15:00"));
    }

    @Test
    public void testMatchTrueFromDateHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/08/2016-14:00"));
    }

    @Test
    public void testMatchTrueFromDateEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "10/08/2016-15:00"));
    }

    @Test
    public void testMatchTrueUntilDate() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "|10/09/2016-15:00"));
    }

    @Test
    public void testMatchTrueUntilDateHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "|10/08/2016-16:00"));
    }

    @Test
    public void testMatchTrueUntilDateEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertTrue(filter.match(input, "|10/08/2016-15:00"));
    }

    @Test
    public void testMatchFalseBetweenRange() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "10/05/2016-15:00|10/07/2016-15:00"));
    }

    @Test
    public void testMatchFalseBetweenRangeHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "10/08/2016-12:00|10/08/2016-14:00"));
    }

    @Test
    public void testMatchFalseFromDate() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "10/09/2016-15:00"));
    }

    @Test
    public void testMatchFalseFromDateHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "10/08/2016-16:00"));
    }

    @Test
    public void testMatchFalseUntilDate() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "|10/07/2016-15:00"));
    }

    @Test
    public void testMatchFalseUntilDateHours() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        String date = "10/08/2016-15:00";
        input.setFechaCreacion(sdf.parse(date));

        Assert.assertFalse(filter.match(input, "|10/08/2016-14:00"));
    }

}
