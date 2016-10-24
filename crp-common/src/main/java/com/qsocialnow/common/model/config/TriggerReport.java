package com.qsocialnow.common.model.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TriggerReport implements Serializable {

    private static final long serialVersionUID = -1713170760210826322L;

    private String name;

    private Map<String, String> segments = new HashMap<String, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getSegments() {
        return segments;
    }

    public void setSegments(Map<String, String> segments) {
        this.segments = segments;
    }

}
