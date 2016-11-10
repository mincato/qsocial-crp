package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;

import io.searchbox.annotations.JestId;

public class SegmentType extends Segment implements IdentityType {

    @JestId
    private String idSegment;

    @Override
    public String getId() {
        return this.idSegment;
    }

    public void setIdSegment(String idSegment) {
        this.idSegment = idSegment;
    }

    public String getIdSegment() {
        return idSegment;
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.idSegment = id;
    }
}
