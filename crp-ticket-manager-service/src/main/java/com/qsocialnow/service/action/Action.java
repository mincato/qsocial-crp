package com.qsocialnow.service.action;

import java.util.Map;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;

public interface Action {

    ActionRegistry execute(Case caseObject, Map<ActionParameter, Object> map);

}
