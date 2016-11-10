package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Media;

public class UserResolverBySource {

    private Media source;

    @NotNull(message = "app.userResolver.null.validation")
    private BaseUserResolver selectedUserResolver;

    public Media getSource() {
        return source;
    }

    public void setSource(Media source) {
        this.source = source;
    }

    public BaseUserResolver getSelectedUserResolver() {
        return selectedUserResolver;
    }

    public void setSelectedUserResolver(BaseUserResolver selectedUserResolver) {
        this.selectedUserResolver = selectedUserResolver;
    }

}
