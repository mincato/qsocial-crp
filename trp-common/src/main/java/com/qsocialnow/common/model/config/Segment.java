package com.qsocialnow.common.model.config;

import java.util.List;

public class Segment {

    private String id;

    private String description;

    private Team team;

    private List<DetectionCriteria> detectionCriterias;

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<DetectionCriteria> getDetectionCriterias() {
        return detectionCriterias;
    }

    public void setDetectionCriterias(List<DetectionCriteria> detectionCriterias) {
        this.detectionCriterias = detectionCriterias;
    }
}
