package com.qsocialnow.viewmodel.team;

import com.qsocialnow.common.model.config.UserResolverListView;

public class TeamUserResolverView {

    private UserResolverListView user;

    private Boolean editingStatus = Boolean.FALSE;

    public UserResolverListView getUser() {
        return user;
    }

    public void setUser(UserResolverListView user) {
        this.user = user;
    }

    public Boolean getEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(Boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
