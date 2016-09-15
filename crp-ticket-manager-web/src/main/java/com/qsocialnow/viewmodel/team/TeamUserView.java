package com.qsocialnow.viewmodel.team;

import com.qsocialnow.common.model.config.UserListView;

public class TeamUserView {

    private UserListView user;

    private Boolean coordinator = Boolean.FALSE;

    public UserListView getUser() {
        return user;
    }

    public void setUser(UserListView user) {
        this.user = user;
    }

    public Boolean getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Boolean coordinator) {
        this.coordinator = coordinator;
    }

}
