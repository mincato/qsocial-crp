package com.qsocialnow.eventresolver.action;

import java.util.Map;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

public interface Action {

    Case execute(Case caseObject, Map<ActionParameter, Object> parameters, ExecutionMessageRequest request);

}
