package com.qsocialnow.viewmodel.team;

import com.qsocialnow.common.model.config.UserResolverListView;

public class TeamUserResolverView {

    private UserResolverListView user;

    private boolean editingStatus = false;

    public UserResolverListView getUser() {
        return user;
    }

    public void setUser(UserResolverListView user) {
        this.user = user;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
