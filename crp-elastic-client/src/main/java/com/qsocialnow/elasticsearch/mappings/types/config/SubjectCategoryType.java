package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.SubjectCategory;

import io.searchbox.annotations.JestId;

public class SubjectCategoryType extends SubjectCategory {

    @JestId
    private String idSubjectCategory;

    private String idSubjectCategorySet;

    public String getIdSubjectCategorySet() {
        return idSubjectCategorySet;
    }

    public void setIdSubjectCategorySet(String idSubjectCategorySet) {
        this.idSubjectCategorySet = idSubjectCategorySet;
    }

    @Override
    public String getId() {
        return this.idSubjectCategory;
    }
}
