package com.qsocialnow.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Media;

public class UserResolverBySource {

    private Media source;

    @NotNull(message = "{userResolver.null}")
    private BaseUserResolver selectedUserResolver;

    private List<BaseUserResolver> userResolverOptions;

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

    public List<BaseUserResolver> getUserResolverOptions() {
        return userResolverOptions;
    }

    public void setUserResolverOptions(List<BaseUserResolver> userResolverOptions) {
        this.userResolverOptions = userResolverOptions;
    }

}
