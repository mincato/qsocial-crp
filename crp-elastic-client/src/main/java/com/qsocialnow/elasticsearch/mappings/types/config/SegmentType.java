package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Segment;

import io.searchbox.annotations.JestId;

public class SegmentType extends Segment {

    @JestId
    private String idSegment;

    private String triggerId;

    @Override
    public String getId() {
        return this.idSegment;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getTriggerId() {
        return triggerId;
    }
}
