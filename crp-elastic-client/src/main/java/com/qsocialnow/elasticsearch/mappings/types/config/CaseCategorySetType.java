package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.CaseCategorySet;

import io.searchbox.annotations.JestId;

public class CaseCategorySetType extends CaseCategorySet {

    @JestId
    private String idCaseCategorySet;

    @Override
    public String getId() {
        return this.idCaseCategorySet;
    }

}
