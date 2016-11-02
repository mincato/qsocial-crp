package com.qsocialnow.common.model.config;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.cases.ActionParameter;

public class AutomaticActionCriteria {

    private String id;

    private Map<ActionParameter, Object> parameters;

    private Integer sequenceOrder;

    private ActionType actionType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<ActionParameter, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<ActionParameter, Object> parameters) {
        this.parameters = parameters;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        if (parameters != null && parameters.containsKey(ActionParameter.COMMENT)) {
            return (String) parameters.get(ActionParameter.COMMENT);
        }
        if (parameters != null && parameters.containsKey(ActionParameter.TEXT)) {
            return (String) parameters.get(ActionParameter.TEXT);
        }
        return "";
    }

}
