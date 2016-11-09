package com.qsocialnow.service.action;

import java.util.Map;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;

public interface AsyncAction extends Action {

    public void postProcess(Case caseObject, Map<ActionParameter, Object> map, ActionRegistry actionRegistry);

}
