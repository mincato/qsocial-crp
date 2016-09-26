package com.qsocialnow.model;

import com.qsocialnow.common.model.config.Resolution;

public class TriggerResolutionView {

    private Resolution resolution;

    private boolean editingStatus = false;

    public Resolution getResolution() {
        return resolution;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

}
