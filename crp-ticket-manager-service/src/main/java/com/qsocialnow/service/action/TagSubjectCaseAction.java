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
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        List<String[]> added = (List<String[]>) parameters.get(ActionParameter.CATEGORIES_ADDED);
        List<String[]> removed = (List<String[]>) parameters.get(ActionParameter.CATEGORIES_REMOVED);
        Subject subject = subjectRepository.findOne(caseObject.getSubject().getId());
        if (subject.getSubjectCategorySet() == null) {
            subject.setSubjectCategorySet(new HashSet<>());
        }
        if (subject.getSubjectCategory() == null) {
            subject.setSubjectCategory(new HashSet<>());
        }
        for (String[] tuple : added) {
            subject.getSubjectCategorySet().add(tuple[0]);
            subject.getSubjectCategory().add(tuple[1]);
        }
        for (String[] tuple : removed) {
            subject.getSubjectCategory().remove(tuple[1]);
        }

        subjectRepository.update(subject);
        return true;
    }

}
