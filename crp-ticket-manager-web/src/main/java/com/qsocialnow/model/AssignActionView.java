package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.BaseUserResolver;

public class AssignActionView {

    @NotNull(message = "{userResolver.null}")
    private BaseUserResolver selectedUserResolver;

    public BaseUserResolver getSelectedUserResolver() {
        return selectedUserResolver;
    }

    public void setSelectedUserResolver(BaseUserResolver selectedUserResolver) {
        this.selectedUserResolver = selectedUserResolver;
    }

}
