package com.qsocialnow.eventresolver.mocks;

import java.util.ArrayList;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;

public class MockDomainBuilder {

    public static Domain buildWithTwoSegmentsOneDetectionCriteriaOnFirst() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        segment = new Segment();
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;
    }

    public static Domain buildWithTwoSegmentsOneDetectionCriteriaOnSecond() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        segments.add(segment);
        segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;
    }

    public static Domain buildWithTwoTriggersOneDetectionCriteriaOnFirst() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        trigger = new Trigger();
        segments = new ArrayList<>();
        segment = new Segment();
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;

    }

    public static Domain buildWithTwoTriggersOneDetectionCriteriaOnSecond() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        segments.add(segment);
        segment = new Segment();
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        triggers = new ArrayList<>();
        trigger = new Trigger();
        segments = new ArrayList<>();
        segment = new Segment();
        segments.add(segment);
        segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;
    }

    public static Domain buildWithTwoSegmentsTwoDetectionCriterias() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        segment = new Segment();
        detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;
    }

    public static Domain buildWithTwoTriggersTwoSegmentsTwoDetectionCriterias() {
        Domain domain = new Domain();
        ArrayList<Trigger> triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment();
        ArrayList<DetectionCriteria> detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        segment = new Segment();
        detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        trigger = new Trigger();
        segments = new ArrayList<>();
        segment = new Segment();
        detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        segment = new Segment();
        detectionCriterias = new ArrayList<>();
        detectionCriterias.add(new DetectionCriteria());
        segment.setDetectionCriterias(detectionCriterias);
        segments.add(segment);
        trigger.setSegment(segments);
        triggers.add(trigger);
        domain.setTriggers(triggers);
        return domain;
    }

}
