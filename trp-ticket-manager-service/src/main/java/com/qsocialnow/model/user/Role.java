/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qsocialnow.model.user;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qsocialnow.model.BackEndEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BackEndEntity {

    private static final long serialVersionUID = -655321606681663452L;

    @NotNull
    private String name;

    @NotNull
    private List<Permission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
