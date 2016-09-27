package com.qsocialnow.service.action;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("tagCaseAction")
public class TagCaseAction implements Action {

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        List<String> caseCategoriesSet = (List<String>) parameters.get(ActionParameter.CATEGORIES_SET);
        if (caseObject.getCaseCategoriesSet() == null) {
            caseObject.setCaseCategoriesSet(new HashSet<>());
        }
        caseObject.getCaseCategoriesSet().addAll(caseCategoriesSet);
        List<String> caseCategories = (List<String>) parameters.get(ActionParameter.CATEGORIES);
        if (caseObject.getCaseCategories() == null) {
            caseObject.setCaseCategories(new HashSet<>());
        }
        caseObject.getCaseCategories().addAll(caseCategories);
        return true;
    }

}
