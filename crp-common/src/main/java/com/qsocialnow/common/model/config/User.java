package com.qsocialnow.common.model.config;

public class User extends BaseUser {

    private static final long serialVersionUID = -5434119218155239394L;

    private boolean coordinator;

    public boolean isCoordinator() {
        return coordinator;
    }

    public void setCoordinator(boolean coordinator) {
        this.coordinator = coordinator;
    }
}
