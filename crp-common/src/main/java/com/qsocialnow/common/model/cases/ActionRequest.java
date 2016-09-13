package com.qsocialnow.common.model.cases;

import java.util.Map;

import com.qsocialnow.common.model.config.ActionType;

public class ActionRequest {

    private ActionType actionType;

    private Map<ActionParameter, Object> parameters;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Map<ActionParameter, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<ActionParameter, Object> parameters) {
        this.parameters = parameters;
    }

}
