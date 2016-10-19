package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.cases.Priority;

public class ChangePriorityActionView {

    @NotNull(message = "{priority.null}")
    private Priority selectedPriority;

    public Priority getSelectedPriority() {
        return selectedPriority;
    }

    public void setSelectedPriority(Priority selectedPriority) {
        this.selectedPriority = selectedPriority;
    }
}
