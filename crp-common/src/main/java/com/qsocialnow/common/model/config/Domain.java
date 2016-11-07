package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class Domain {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String name;

    private List<Trigger> triggers;

    private List<Long> thematics;

    private List<Resolution> resolutions;

    private boolean active = true;

    public Domain() {
        triggers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getThematics() {
        return thematics;
    }

    public void setThematics(List<Long> thematics) {
        this.thematics = thematics;
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public List<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
