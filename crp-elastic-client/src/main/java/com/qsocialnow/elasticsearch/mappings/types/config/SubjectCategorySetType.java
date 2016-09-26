package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.SubjectCategorySet;

import io.searchbox.annotations.JestId;

public class SubjectCategorySetType extends SubjectCategorySet {

    @JestId
    private String idSubjectCategorySet;

    @Override
    public String getId() {
        return this.idSubjectCategorySet;
    }
}
