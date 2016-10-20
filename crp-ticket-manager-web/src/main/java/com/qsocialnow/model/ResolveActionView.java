package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.Resolution;

public class ResolveActionView {

    @NotNull(message = "cases.resolutions.null.validation")
    private Resolution selectedResolution;

    public Resolution getSelectedResolution() {
        return selectedResolution;
    }

    public void setSelectedResolution(Resolution selectedResolution) {
        this.selectedResolution = selectedResolution;
    }

}
