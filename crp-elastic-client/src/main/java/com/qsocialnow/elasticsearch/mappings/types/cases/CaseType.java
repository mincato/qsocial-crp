package com.qsocialnow.elasticsearch.mappings.types.cases;

import com.qsocialnow.common.model.cases.Case;

import io.searchbox.annotations.JestId;

public class CaseType extends Case {

    @JestId
    private String idCase;

    @Override
    public String getId() {
        return this.idCase;
    }
}
