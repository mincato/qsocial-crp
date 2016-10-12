package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersByClientOrganizationIdOutput {

    private Integer clientOrganizationId;

    private List<User> users = new ArrayList<User>();

    public Integer getClientOrganizationId() {
        return clientOrganizationId;
    }

    public void setClientOrganizationId(Integer clientOrganizationId) {
        this.clientOrganizationId = clientOrganizationId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
