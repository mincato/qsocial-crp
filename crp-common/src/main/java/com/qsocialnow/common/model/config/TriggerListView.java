package com.qsocialnow.common.model.config;

import java.io.Serializable;
import java.util.List;

public class TriggerListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String name;

    private String description;

    private Status status;

    private Long fromDate;

    private Long toDate;

    private List<Segment> segments;

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

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}