package com.qsocialnow.service.action;

import java.util.Map;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

public interface Action {

    AsyncAction execute(Case caseObject, Map<ActionParameter, Object> map);

}
