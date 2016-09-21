package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class Segment {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String description;

    private String team;

    private List<DetectionCriteria> detectionCriterias;

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

    public List<DetectionCriteria> getDetectionCriterias() {
        return detectionCriterias;
    }

    public void setDetectionCriterias(List<DetectionCriteria> detectionCriterias) {
        this.detectionCriterias = detectionCriterias;
    }
}
