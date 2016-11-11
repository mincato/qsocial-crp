package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class TeamListView implements Serializable {

    private static final long serialVersionUID = -7354700566658675263L;

    private String id;

    private String name;

    private boolean active = true;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}