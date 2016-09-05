package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Resolution;

import io.searchbox.annotations.JestId;

public class ResolutionType extends Resolution {

    @JestId
    private String idResolution;

    @Override
    public String getId() {
        return this.idResolution;
    }

}
