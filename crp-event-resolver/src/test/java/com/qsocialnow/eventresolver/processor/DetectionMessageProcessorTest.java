package com.qsocialnow.eventresolver.processor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.mocks.MockDomainBuilder;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@RunWith(MockitoJUnitRunner.class)
public class DetectionMessageProcessorTest {

    @Mock
    private DetectionCriteriaResolver detectionCriteriaResolver;

    @InjectMocks
    private DetectionMessageProcessor detectionMessageProcessor;

    public DetectionMessageProcessorTest() {
        this.detectionMessageProcessor = new DetectionMessageProcessor();
    }

    @Test
    public void testDetectNull() {
        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, null);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testDetectDomainNull() {
        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(new InPutBeanDocument(), null);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testDetectTriggersNull() {
        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, new Domain());

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testDetectSegmentsNull() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        triggers.add(new Trigger());
        domain.setTriggers(triggers);
        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNull(detectionCriteria);
    }

    @Test
    public void testDetectFirstDetectionCriteriaTwoSegments() {
        Domain domain = MockDomainBuilder.buildWithTwoSegmentsOneDetectionCriteriaOnFirst();
        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(new DetectionCriteria());

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNotNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectSecondDetectionCriteriaTwoSegments() {
        Domain domain = MockDomainBuilder.buildWithTwoSegmentsOneDetectionCriteriaOnSecond();

        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        (List<DetectionCriteria>) Mockito.isNull(List.class))).thenReturn(null);
        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(new DetectionCriteria());

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNotNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), (List<DetectionCriteria>) Mockito.isNull(List.class));
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectFirstDetectionCriteriaTwoTriggers() {
        Domain domain = MockDomainBuilder.buildWithTwoTriggersOneDetectionCriteriaOnFirst();
        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(new DetectionCriteria());

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNotNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectSecondDetectionCriteriaTwoTriggers() {
        Domain domain = MockDomainBuilder.buildWithTwoTriggersOneDetectionCriteriaOnSecond();

        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        (List<DetectionCriteria>) Mockito.isNull(List.class))).thenReturn(null);
        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(new DetectionCriteria());

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNotNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), (List<DetectionCriteria>) Mockito.isNull(List.class));
        Mockito.verify(detectionCriteriaResolver, Mockito.times(1)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectTwoTriggersNoMatch() {
        Domain domain = MockDomainBuilder.buildWithTwoTriggersOneDetectionCriteriaOnFirst();

        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(null);

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(2)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectTwoSegmentsNoMatch() {
        Domain domain = MockDomainBuilder.buildWithTwoSegmentsTwoDetectionCriterias();

        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(null);

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(2)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

    @Test
    public void testDetectTwoTriggersTwoSegmentsNoMatch() {
        Domain domain = MockDomainBuilder.buildWithTwoTriggersTwoSegmentsTwoDetectionCriterias();

        Mockito.when(
                detectionCriteriaResolver.resolve(Mockito.any(NormalizedInputBeanDocument.class),
                        Mockito.anyListOf(DetectionCriteria.class))).thenReturn(null);

        DetectionCriteria detectionCriteria = detectionMessageProcessor.detect(null, domain);

        Assert.assertNull(detectionCriteria);
        Mockito.verify(detectionCriteriaResolver, Mockito.times(4)).resolve(
                Mockito.any(NormalizedInputBeanDocument.class), Mockito.anyListOf(DetectionCriteria.class));
    }

}
