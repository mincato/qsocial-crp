package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("registerSubjectReplyCaseAction")
public class RegisterSubjectReplyCaseAction implements Action {

    @Override
    public AsyncAction execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        parameters.put(ActionParameter.COMMENT, text);
        return null;
    }

}
