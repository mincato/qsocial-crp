package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.User;

public class AssignActionView {

    @NotNull(message = "{user.null}")
    private User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
