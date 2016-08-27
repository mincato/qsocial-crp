package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class DomainListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String name;

    private String thematics;

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

    public String getThematics() {
        return thematics;
    }

    public void setThematics(String thematics) {
        this.thematics = thematics;
    }

}