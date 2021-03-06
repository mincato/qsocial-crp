package com.qsocialnow.elasticsearch.mappings.types.cases;

import com.qsocialnow.common.model.cases.Case;

import io.searchbox.annotations.JestId;

public class CaseType extends Case implements IdentityType {

    @JestId
    private String idCase;

    private Integer priorityOrder;

    @Override
    public String getId() {
        return this.idCase;
    }

    public void setIdCase(String id) {
        this.idCase = id;
    }

    public Integer getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }
}
