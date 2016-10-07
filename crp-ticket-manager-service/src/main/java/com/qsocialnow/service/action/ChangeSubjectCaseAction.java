package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.persistence.SubjectRepository;

@Component("changeSubjectCaseAction")
public class ChangeSubjectCaseAction implements Action {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String subjectId = (String) parameters.get(ActionParameter.SUBJECT);
        Subject subject = subjectRepository.findOne(subjectId);
        Subject oldSubject = caseObject.getSubject();
        caseObject.setSubject(subject);
        caseObject.setSource(subject.getSource());
        parameters.put(ActionParameter.COMMENT, buildComment(oldSubject, subject));
        return true;
    }

    private String buildComment(Subject oldSubject, Subject subject) {
        StringBuilder sb = new StringBuilder();
        if (oldSubject != null) {
            sb.append(oldSubject.getIdentifier());
            sb.append(" --> ");
        }
        sb.append(subject.getIdentifier());
        return sb.toString();
    }

}
