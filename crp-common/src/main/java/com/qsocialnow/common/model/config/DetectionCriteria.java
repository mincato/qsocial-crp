package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class DetectionCriteria {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String name;

    private Date validateFrom;

    private Date validateTo;

    private Integer sequenceOrder;

    private Filter filter;

    private boolean alwaysOpenCase;

    private boolean findCaseOnAllDomains;

    private List<AutomaticActionCriteria> actionCriterias;

    public DetectionCriteria() {
        actionCriterias = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AutomaticActionCriteria> getActionCriterias() {
        return actionCriterias;
    }

    public void setActionCriterias(List<AutomaticActionCriteria> actionCriterias) {
        this.actionCriterias = actionCriterias;
    }

    public boolean isAlwaysOpenCase() {
        return alwaysOpenCase;
    }

    public void setAlwaysOpenCase(boolean alwaysOpenCase) {
        this.alwaysOpenCase = alwaysOpenCase;
    }

    public boolean isFindCaseOnAllDomains() {
        return findCaseOnAllDomains;
    }

    public void setFindCaseOnAllDomains(boolean findCaseOnAllDomains) {
        this.findCaseOnAllDomains = findCaseOnAllDomains;
    }

}
