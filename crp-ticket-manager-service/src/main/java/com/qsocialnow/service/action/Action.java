package com.qsocialnow.service.action;

import java.util.Map;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

public interface Action {

    void execute(Case caseObject, Map<ActionParameter, Object> map);

}
