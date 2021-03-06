package com.qsocialnow.eventresolver.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.mocks.FalseDetectionCriteriaFilter;
import com.qsocialnow.eventresolver.mocks.MockDetectionCriteriaBuilder;
import com.qsocialnow.eventresolver.mocks.TrueDetectionCriteriaFilter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class DetectionCriteriaResolverTest {

    private DetectionCriteriaResolver detectionCriteriaResolver;

    public DetectionCriteriaResolverTest() {
        this.detectionCriteriaResolver = new DetectionCriteriaResolver();
        this.detectionCriteriaResolver.setFilters(Arrays.asList(new TrueDetectionCriteriaFilter(),
                new FalseDetectionCriteriaFilter()));
    }

    @Test
    public void testResolveNull() {
        List<DetectionCriteria> detectionCriterias = null;

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testResolveEmpty() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testResolveOneFalse() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildOneFalse();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testResolveOneTrue() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildOneTrue();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertEquals("1", detectionCriteria.getId());
    }

    @Test
    public void testResolveOneTrueInvalid() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildOneTrueInvalid();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testResolveTwoFalseTrue() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildTwoFalseTrue();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertEquals("2", detectionCriteria.getId());
    }

    @Test
    public void testResolveTwoTrueFalse() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildTwoTrueFalse();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertEquals("1", detectionCriteria.getId());
    }

    @Test
    public void testResolveThree() {
        List<DetectionCriteria> detectionCriterias = MockDetectionCriteriaBuilder.buildThree();

        DetectionCriteria detectionCriteria = detectionCriteriaResolver.resolve(new NormalizedInputBeanDocument(
                new Event()), detectionCriterias);

        Assert.assertEquals("2", detectionCriteria.getId());
    }

}
