package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Priority;

@Component("changePriorityCaseAction")
public class ChangePriorityCaseAction implements Action {

    @Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String priorityParameter = (String) parameters.get(ActionParameter.PRIORITY);
        Priority priority = Priority.valueOf(priorityParameter);
        Priority oldPriority = caseObject.getPriority();
        caseObject.setPriority(priority);
        parameters.put(ActionParameter.COMMENT, buildComment(oldPriority, priority));
    }

    private String buildComment(Priority oldPriority, Priority priority) {
        StringBuilder sb = new StringBuilder();
        if (oldPriority != null) {
            sb.append(oldPriority.name());
            sb.append(" --> ");
        }
        sb.append(priority);
        return sb.toString();
    }

}
