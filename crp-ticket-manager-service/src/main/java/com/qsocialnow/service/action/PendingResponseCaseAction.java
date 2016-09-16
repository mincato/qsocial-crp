package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("pendingResponseCaseAction")
public class PendingResponseCaseAction implements Action {

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setPendingResponse(true);
        return true;
    }

}
