package com.qsocialnow.common.model.config;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class UserListView implements Serializable {

    private static final long serialVersionUID = -2846467480690098368L;

    private Integer id;

    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserListView ulv = (UserListView) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, ulv.id).isEquals();
    }

}