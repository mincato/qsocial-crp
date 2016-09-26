package com.qsocialnow.viewmodel.team;

import com.qsocialnow.common.model.config.UserListView;

public class TeamUserView {

    private UserListView user;

    private boolean coordinator = false;

    private boolean editingStatus = false;

    public UserListView getUser() {
        return user;
    }

    public void setUser(UserListView user) {
        this.user = user;
    }

    public boolean isCoordinator() {
        return coordinator;
    }

    public void setCoordinator(boolean coordinator) {
        this.coordinator = coordinator;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
