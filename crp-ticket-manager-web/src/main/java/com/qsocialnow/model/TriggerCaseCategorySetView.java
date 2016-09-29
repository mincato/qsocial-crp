package com.qsocialnow.model;

import com.qsocialnow.common.model.config.CaseCategorySet;

public class TriggerCaseCategorySetView {

    private CaseCategorySet caseCategorySet;

    private boolean editingStatus = false;

    public CaseCategorySet getCaseCategorySet() {
        return caseCategorySet;
    }

    public void setCaseCategorySet(CaseCategorySet caseCategorySet) {
        this.caseCategorySet = caseCategorySet;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
