package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Segment;

import io.searchbox.annotations.JestId;

public class SegmentType extends Segment {

    @JestId
    private String idSegment;

    @Override
    public String getId() {
        return this.idSegment;
    }
}
