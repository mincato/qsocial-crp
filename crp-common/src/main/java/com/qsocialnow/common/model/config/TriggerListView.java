package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class TriggerListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String name;

    private String description;

    private Status status;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}