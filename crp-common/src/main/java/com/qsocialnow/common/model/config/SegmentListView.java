package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class SegmentListView implements Serializable {

    private static final long serialVersionUID = 2248712249646839694L;

    private String id;

    private String description;

    private String teamId;

    private boolean active = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}