package com.qsocialnow.service.action;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("tagCaseAction")
public class TagCaseAction implements Action {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        List<String> caseCategoriesSet = (List<String>) parameters.get(ActionParameter.CATEGORIES_SET);
        caseObject.setCaseCategoriesSet(new HashSet<>(caseCategoriesSet));
        List<String> caseCategories = (List<String>) parameters.get(ActionParameter.CATEGORIES);
        caseObject.setCaseCategories(new HashSet<>(caseCategories));
    }

}
