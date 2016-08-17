/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qsocialnow.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qsocialnow.model.BackEndEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission extends BackEndEntity {

    private static final long serialVersionUID = -3553958359504695800L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
