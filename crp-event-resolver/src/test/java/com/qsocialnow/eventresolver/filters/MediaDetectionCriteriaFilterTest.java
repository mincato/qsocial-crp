package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

public class MediaDetectionCriteriaFilterTest {

    private MediaDetectionCriteriaFilter filter;

    public MediaDetectionCriteriaFilterTest() {
        filter = new MediaDetectionCriteriaFilter();
    }

    @Test
    public void testMatchParameterNull() {
        Assert.assertFalse(filter.match(null, null));
    }

    @Test
    public void testMatchTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        Assert.assertTrue(filter.match(input, "1"));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        Assert.assertTrue(filter.match(input, "1|2"));
    }

    @Test
    public void testMatchFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        Assert.assertFalse(filter.match(input, "2"));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setMedioId(1L);

        Assert.assertFalse(filter.match(input, "2|3"));
    }

}
