package com.qsocialnow.model;

import javax.validation.Valid;

import com.qsocialnow.common.model.cases.Case;

public class CaseView {

    @Valid
    private Case newCase;

    public CaseView() {
    }

    public Case getNewCase() {
        return newCase;
    }

    public void setNewCase(Case newCase) {
        this.newCase = newCase;
    }
}
