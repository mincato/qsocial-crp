package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Subject;

import io.searchbox.annotations.JestId;

public class SubjectCategorySetType extends Subject {

    @JestId
    private String idSubjectCategorySet;

    @Override
    public String getId() {
        return this.idSubjectCategorySet;
    }
}
