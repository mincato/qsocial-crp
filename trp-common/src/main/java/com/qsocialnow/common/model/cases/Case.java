package com.qsocialnow.common.model.cases;

import java.util.Date;
import java.util.List;

public class Case {

    private String id;

    private Boolean open;

    private Boolean pendingResponse;

    private Date openDate;

    private Date closeDate;

    private String title;

    private String description;

    private Event triggerEvent;

    private Coordinates coordinates;

    private Coordinates adminUnits;

    private Integer resolution;

    private Integer asignedValue;

    private String unitValue;

    private Customer customer;

    private List<Long> caseCategories;

    private List<ActionRegistry> actionsRegistry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(Boolean pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Event getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(Event triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(Coordinates adminUnits) {
        this.adminUnits = adminUnits;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Integer getAsignedValue() {
        return asignedValue;
    }

    public void setAsignedValue(Integer asignedValue) {
        this.asignedValue = asignedValue;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public List<Long> getCaseCategories() {
        return caseCategories;
    }

    public void setCaseCategories(List<Long> caseCategories) {
        this.caseCategories = caseCategories;
    }

    public List<ActionRegistry> getActionsRegistry() {
        return actionsRegistry;
    }

    public void setActionsRegistry(List<ActionRegistry> actionsRegistry) {
        this.actionsRegistry = actionsRegistry;
    }

}
