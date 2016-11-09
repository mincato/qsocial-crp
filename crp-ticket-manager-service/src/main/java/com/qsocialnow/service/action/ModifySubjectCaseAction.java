package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.persistence.SubjectRepository;

@Component("modifySubjectCaseAction")
public class ModifySubjectCaseAction implements Action {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public AsyncAction execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String subjectJson = (String) parameters.get(ActionParameter.SUBJECT);
        Subject subject = new GsonBuilder().create().fromJson(subjectJson, Subject.class);
        if (subjectRepository.update(subject)) {
            caseObject.setSubject(subject);
        } else {
            throw new RuntimeException("There was an error modifying subject");
        }
        return null;
    }

}
