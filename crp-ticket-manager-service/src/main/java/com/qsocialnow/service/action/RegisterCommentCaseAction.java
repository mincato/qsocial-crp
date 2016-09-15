package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("registerCommentCaseAction")
public class RegisterCommentCaseAction implements Action {

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        return false;
    }

}
