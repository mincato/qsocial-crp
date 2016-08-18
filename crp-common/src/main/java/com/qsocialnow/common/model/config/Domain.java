package com.qsocialnow.common.model.config;

import java.util.List;

public class Domain {

    private String id;

    private String name;

    private List<Trigger> triggers;

    private List<Long> thematics;

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
}
