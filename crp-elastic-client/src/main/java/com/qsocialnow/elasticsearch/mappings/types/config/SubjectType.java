package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;

import io.searchbox.annotations.JestId;

public class SubjectType extends Subject implements IdentityType{

    private static final long serialVersionUID = 1L;

    @JestId
    private String idSubject;

    @Override
    public String getId() {
        return this.idSubject;
    }
}
