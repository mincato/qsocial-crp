package com.qsocialnow.service.action;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;

@Component("pendingResponseCaseAction")
public class PendingResponseCaseAction implements Action {

    @Override
    public ActionRegistry execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setPendingResponse(true);
        ActionRegistry actionRegistry = buildActionRegistry(parameters);
        return actionRegistry;
    }

    private ActionRegistry buildActionRegistry(Map<ActionParameter, Object> parameters) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setType(ActionType.PENDING_RESPONSE);
        actionRegistry.setDate(new Date());
        return actionRegistry;
    }

}
