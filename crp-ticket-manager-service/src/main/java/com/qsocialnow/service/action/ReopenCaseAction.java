package com.qsocialnow.service.action;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;

@Component("reopenCaseAction")
public class ReopenCaseAction implements Action {

    @Override
    public ActionRegistry execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setCloseDate(null);
        caseObject.setOpen(true);
        ActionRegistry actionRegistry = buildActionRegistry(parameters);
        return actionRegistry;
    }

    private ActionRegistry buildActionRegistry(Map<ActionParameter, Object> parameters) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setType(ActionType.REOPEN);
        actionRegistry.setDate(new Date());
        Object comment = parameters.get(ActionParameter.COMMENT);
        if (comment != null) {
            actionRegistry.setComment((String) comment);
        }
        return actionRegistry;
    }

}
