package com.qsocialnow.common.model.config;

import java.util.Date;
import java.util.List;

public class DetectionCriteria {

    private String id;

    private Date validateFrom;

    private Date validateTo;

    private Integer sequenceOrder;

    private Filter filter;

    private List<AutomaticActionCriteria> actionCriterias;

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

    public List<AutomaticActionCriteria> getActionCriterias() {
        return actionCriterias;
    }

    public void setAccionCriterias(List<AutomaticActionCriteria> actionCriterias) {
        this.actionCriterias = actionCriterias;
    }
}
