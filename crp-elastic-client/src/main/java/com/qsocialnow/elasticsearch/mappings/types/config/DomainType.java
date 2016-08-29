package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Domain;

import io.searchbox.annotations.JestId;

public class DomainType extends Domain {

    @JestId
    private String idEntity;

    @Override
    public String getId() {
        return this.idEntity;
    }

    public void setIdEntity(String id) {
        this.idEntity = id;
    }
}
