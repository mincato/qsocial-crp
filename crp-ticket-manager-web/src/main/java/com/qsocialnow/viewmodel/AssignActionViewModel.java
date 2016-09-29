package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.model.AssignActionView;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.TeamService;

@VariableResolver(DelegatingVariableResolver.class)
public class AssignActionViewModel implements Serializable {

    private static final long serialVersionUID = 6188170388292392609L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private TeamService teamService;

    private String caseId;

    private List<User> userOptions;

    private AssignActionView assignAction;

    private EditCaseView currentCase;

    @Init
    public void init(@QueryParam("case") String caseId) {
        assignAction = new AssignActionView();
        this.caseId = caseId;
    }

    public List<User> getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(List<User> userOptions) {
        this.userOptions = userOptions;
    }

    public AssignActionView getAssignAction() {
        return assignAction;
    }

    public void setAssignAction(AssignActionView assignAction) {
        this.assignAction = assignAction;
    }

    @GlobalCommand
    @NotifyChange({ "assignAction", "userOptions" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.ASSIGN.equals(action)) {
            this.currentCase = currentCase;
            this.assignAction = new AssignActionView();
            if (currentCase.getUserOptions() == null) {
                initUsers(currentCase);
            }
            this.userOptions = new ArrayList<>(currentCase.getUserOptions());
            if (currentCase.getCaseObject().getAssignee() != null) {
                for (Iterator<User> iterator = userOptions.iterator(); iterator.hasNext();) {
                    User user = iterator.next();
                    if (user.getId().equals(currentCase.getCaseObject().getAssignee().getId())) {
                        iterator.remove();
                        break;
                    }

                }
            }
        }
    }

    private void initUsers(EditCaseView currentCase) {
        List<User> users = teamService.findUsers(currentCase.getSegment().getTeam());
        currentCase.setUserOptions(users);
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.ASSIGN);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.USER, assignAction.getSelectedUser().getId());
        parameters.put(ActionParameter.TEAM, currentCase.getSegment().getTeam());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
