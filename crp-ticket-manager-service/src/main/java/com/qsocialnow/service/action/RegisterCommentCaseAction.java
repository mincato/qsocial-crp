package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("registerCommentCaseAction")
public class RegisterCommentCaseAction implements Action {

    @Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
    }

}
