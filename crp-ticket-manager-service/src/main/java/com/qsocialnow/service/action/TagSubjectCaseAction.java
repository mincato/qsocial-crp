package com.qsocialnow.service.action;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.persistence.SubjectRepository;

@Component("tagSubjectCaseAction")
public class TagSubjectCaseAction implements Action {

    @Autowired
    private SubjectRepository subjectRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        List<String> subjectCategoriesSet = (List<String>) parameters.get(ActionParameter.CATEGORIES_SET);
        caseObject.getSubject().setSubjectCategorySet(new HashSet<>(subjectCategoriesSet));
        List<String> subjectCategories = (List<String>) parameters.get(ActionParameter.CATEGORIES);
        caseObject.getSubject().setSubjectCategory(new HashSet<>(subjectCategories));
        List<List<String>> added = (List<List<String>>) parameters.get(ActionParameter.CATEGORIES_ADDED);
        List<List<String>> removed = (List<List<String>>) parameters.get(ActionParameter.CATEGORIES_REMOVED);
        Subject subject = subjectRepository.findOne(caseObject.getSubject().getId());
        if (subject.getSubjectCategorySet() == null) {
            subject.setSubjectCategorySet(new HashSet<>());
        }
        if (subject.getSubjectCategory() == null) {
            subject.setSubjectCategory(new HashSet<>());
        }
        for (List<String> tuple : added) {
            subject.getSubjectCategorySet().add(tuple.get(0));
            subject.getSubjectCategory().add(tuple.get(1));
        }
        for (List<String> tuple : removed) {
            subject.getSubjectCategory().remove(tuple.get(1));
        }

        subjectRepository.update(subject);
    }

}
