package com.qsocialnow.service.action;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("closeCaseAction")
public class CloseCaseAction implements Action {

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setCloseDate(new Date().getTime());
        caseObject.setOpen(false);
        return true;
    }

}
