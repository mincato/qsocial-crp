package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetectionCriteria {

    private String id;

    private String name;

    private Date validateFrom;

    private Date validateTo;

    private Integer sequenceOrder;

    private Filter filter;

    private List<Filter> filters;

    private List<AutomaticActionCriteria> actionCriterias;

    public DetectionCriteria() {
        this.filters = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getValidateFrom() {
        return validateFrom;
    }

    public void setValidateFrom(Date validateFrom) {
        this.validateFrom = validateFrom;
    }

    public Date getValidateTo() {
        return validateTo;
    }

    public void setValidateTo(Date validateTo) {
        this.validateTo = validateTo;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AutomaticActionCriteria> getActionCriterias() {
        return actionCriterias;
    }

    public void setAccionCriterias(List<AutomaticActionCriteria> actionCriterias) {
        this.actionCriterias = actionCriterias;
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

}
