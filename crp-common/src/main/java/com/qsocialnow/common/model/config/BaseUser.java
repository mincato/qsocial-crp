package com.qsocialnow.common.model.config;

import java.io.Serializable;

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

}
