package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.CaseCategory;

import io.searchbox.annotations.JestId;

public class CaseCategoryType extends CaseCategory {

    @JestId
    private String idCaseCategory;

    private String idCaseCategorySet;

    public String getIdCaseCategorySet() {
        return idCaseCategorySet;
    }

    public void setIdCaseCategorySet(String idCaseCategorySet) {
        this.idCaseCategorySet = idCaseCategorySet;
    }

    @Override
    public String getId() {
        return this.idCaseCategory;
    }

}
