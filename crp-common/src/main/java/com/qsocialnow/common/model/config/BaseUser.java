package com.qsocialnow.common.model.config;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BaseUser implements Serializable {

    private static final long serialVersionUID = 3151356977508560054L;

    private Integer id;

    private String username;

    public BaseUser() {
    }

    public BaseUser(BaseUser user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
    }

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

    public int hashCode() {
        return new HashCodeBuilder(1, 3).append(id).append(username).toHashCode();
    }

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
        BaseUser rhs = (BaseUser) obj;
        return new EqualsBuilder().append(id, rhs.getId()).append(username, rhs.getUsername()).isEquals();
    }
}
