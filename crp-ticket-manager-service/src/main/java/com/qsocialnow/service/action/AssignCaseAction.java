package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.BaseUser;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.persistence.TeamRepository;

@Component("assignCaseAction")
public class AssignCaseAction implements Action {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String userId = (String) parameters.get(ActionParameter.USER);
        String teamId = (String) parameters.get(ActionParameter.TEAM);
        Team team = teamRepository.findOne(teamId);
        BaseUser oldAssignee = caseObject.getAssignee();
        User assignee = team.getUsers().stream().filter(user -> user.getId().equals(userId)).findFirst().get();
        caseObject.setAssignee(new BaseUser(assignee));
        parameters.put(ActionParameter.COMMENT, buildComment(oldAssignee, assignee));
        return true;
    }

    private String buildComment(BaseUser oldUser, User user) {
        StringBuilder sb = new StringBuilder();
        if (oldUser != null) {
            sb.append(oldUser.getUsername());
            sb.append(" --> ");
        }
        sb.append(user.getUsername());
        return sb.toString();
    }

}
