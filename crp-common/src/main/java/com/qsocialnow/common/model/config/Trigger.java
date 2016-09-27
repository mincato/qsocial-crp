package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Trigger {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String name;

    @NotNull(message = "{field.empty}")
    private Date init;

    @NotNull(message = "{field.empty}")
    private Date end;

    private String description;

    private List<Segment> segments;

    private List<CustomerGroup> customerGroups;

    private List<Resolution> resolutions;

    private List<CaseCategorySet> caseCategoriesSet;

    @NotNull(message = "{field.empty}")
    private Status status;

    public Trigger() {
        this.segments = new ArrayList<>();
        this.customerGroups = new ArrayList<>();
        this.resolutions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<CustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public List<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CaseCategorySet> getCaseCategoriesSet() {
        return caseCategoriesSet;
    }

    public void setCaseCategoriesSet(List<CaseCategorySet> caseCategoriesSet) {
        this.caseCategoriesSet = caseCategoriesSet;
    }

}
