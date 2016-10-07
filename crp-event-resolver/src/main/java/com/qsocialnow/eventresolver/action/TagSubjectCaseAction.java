package com.qsocialnow.eventresolver.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.elasticsearch.services.cases.SubjectService;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

@Component("tagSubjectCaseAction")
public class TagSubjectCaseAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(TagSubjectCaseAction.class);

    @Autowired
    private SubjectService subjectService;

    @Override
    public Case execute(Case caseObject, Map<ActionParameter, Object> parameters, ExecutionMessageRequest request) {
        if (caseObject.getSubject() != null) {
            List<String> subjectCategoriesSet = (List<String>) parameters.get(ActionParameter.CATEGORIES_SET);
            caseObject.getSubject().addSubjectCategoriesSet(subjectCategoriesSet);
            List<String> subjectCategories = (List<String>) parameters.get(ActionParameter.CATEGORIES);
            caseObject.getSubject().addSubjectCategories(subjectCategories);
            Subject subject = subjectService.findSubjectById(caseObject.getSubject().getId());
            subject.addSubjectCategoriesSet(subjectCategoriesSet);
            subject.addSubjectCategories(subjectCategories);
            subjectService.update(subject);
        } else {
            log.info("The case does not have a subject to tag");
        }
        return caseObject;
    }

}
