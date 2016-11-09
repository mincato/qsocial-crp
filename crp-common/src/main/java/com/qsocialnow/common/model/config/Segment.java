package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class Segment {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private String team;

    private List<DetectionCriteria> detectionCriterias;

    private String triggerId;

    private boolean active = true;

    public Segment() {
        this.detectionCriterias = new ArrayList<>();
    }

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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public List<DetectionCriteria> getDetectionCriterias() {
        return detectionCriterias;
    }

    public void setDetectionCriterias(List<DetectionCriteria> detectionCriterias) {
        this.detectionCriterias = detectionCriterias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
