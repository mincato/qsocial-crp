package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class UserListView implements Serializable {

    private static final long serialVersionUID = -2846467480690098368L;

    private String id;

    private String name;

    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}