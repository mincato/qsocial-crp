package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.DetectionCriteria;

import io.searchbox.annotations.JestId;

public class DetectionCriteriaType extends DetectionCriteria {

    @JestId
    private String idCriteria;

    @Override
    public String getId() {
        return this.idCriteria;
    }
}
