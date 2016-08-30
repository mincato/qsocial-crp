package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.event.InPutBeanDocument;

public class ConnotationDetectionCriteriaFilterTest {

    private ConnotationDetectionCriteriaFilter filter;

    public ConnotationDetectionCriteriaFilterTest() {
        filter = new ConnotationDetectionCriteriaFilter();
    }

    @Test
    public void testMatchParameterNull() {
        Assert.assertFalse(filter.match(null, null));
    }

    @Test
    public void testMatchTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setConnotacion((short) 1);

        Assert.assertTrue(filter.match(input, "1"));
    }

    @Test
    public void testMatchFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setConnotacion((short) 1);

        Assert.assertFalse(filter.match(input, "2"));
    }

}
