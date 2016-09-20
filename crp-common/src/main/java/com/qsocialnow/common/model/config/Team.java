package com.qsocialnow.common.model.config;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class Team {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String name;

    private List<User> users;

    private List<BaseUserResolver> userResolvers;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<BaseUserResolver> getUserResolvers() {
        return userResolvers;
    }

    public void setUserResolvers(List<BaseUserResolver> userResolvers) {
        this.userResolvers = userResolvers;
    }

}
