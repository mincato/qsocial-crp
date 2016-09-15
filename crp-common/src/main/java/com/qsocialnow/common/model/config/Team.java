package com.qsocialnow.common.model.config;

import java.util.List;

public class Team {

    private String id;

    private String name;

    private List<User> users;

    private List<String> userResolvers;

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

    public List<String> getUserResolvers() {
        return userResolvers;
    }

    public void setUserResolvers(List<String> userResolvers) {
        this.userResolvers = userResolvers;
    }
}
