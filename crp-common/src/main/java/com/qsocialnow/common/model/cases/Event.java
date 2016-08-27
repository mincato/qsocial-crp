package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class Event implements Serializable {

    private static final long serialVersionUID = -8403038270455398736L;

    private String id;

    // TODO rename attribute
    private String topic;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
