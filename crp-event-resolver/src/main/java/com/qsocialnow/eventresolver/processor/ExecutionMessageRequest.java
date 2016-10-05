package com.qsocialnow.eventresolver.processor;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.event.Event;

public class ExecutionMessageRequest {

    private final Event input;

    private final DetectionCriteria detectionCriteria;

    private final Domain domain;

    private final Trigger trigger;

    private final Segment segment;

    public ExecutionMessageRequest(final Event input, final Domain domain, final DetectionCriteria detectionCriteria,
            final Trigger trigger, final Segment segment) {
        this.input = input;
        this.domain = domain;
        this.detectionCriteria = detectionCriteria;
        this.trigger = trigger;
        this.segment = segment;
    }

    public Event getInput() {
        return input;
    }

    public DetectionCriteria getDetectionCriteria() {
        return detectionCriteria;
    }

    public Domain getDomain() {
        return domain;
    }

    public Segment getSegment() {
        return segment;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
