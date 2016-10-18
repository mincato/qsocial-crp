package com.qsocialnow.model;

import com.qsocialnow.common.model.config.AdmUnitFilter;

public class AdmUnitFilterView extends AdmUnitFilter {

    private boolean editingStatus = false;

    public AdmUnitFilterView() {
        // TODO Auto-generated constructor stub
    }

    public AdmUnitFilterView(AdmUnitFilter other) {
        super(other);
        this.editingStatus = false;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
