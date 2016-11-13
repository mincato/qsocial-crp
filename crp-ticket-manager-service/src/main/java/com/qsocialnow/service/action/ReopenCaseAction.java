package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("reopenCaseAction")
public class ReopenCaseAction implements Action {

    @Override
    public AsyncAction execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setCloseDate(null);
        caseObject.setOpen(true);
        return null;
    }

}
