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
import com.qsocialnow.common.model.config.BaseUserResolver;
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

    private List<BaseUserResolver> userResolverOptions;

    private AssignActionView assignAction;

    @Init
    public void init(@QueryParam("case") String caseId) {
        assignAction = new AssignActionView();
        this.caseId = caseId;
    }

    public List<BaseUserResolver> getUserResolverOptions() {
        return userResolverOptions;
    }

    public void setUserResolverOptions(List<BaseUserResolver> userResolverOptions) {
        this.userResolverOptions = userResolverOptions;
    }

    public AssignActionView getAssignAction() {
        return assignAction;
    }

    public void setAssignAction(AssignActionView assignAction) {
        this.assignAction = assignAction;
    }

    @GlobalCommand
    @NotifyChange({ "assignAction", "userResolverOptions" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.ASSIGN.equals(action)) {
            this.assignAction = new AssignActionView();
            if (currentCase.getUserResolverOptions() == null) {
                initUserResolvers(currentCase);
            }
            this.userResolverOptions = new ArrayList<>(currentCase.getUserResolverOptions());
            if (currentCase.getCaseObject().getUserResolver() != null) {
                for (Iterator<BaseUserResolver> iterator = userResolverOptions.iterator(); iterator.hasNext();) {
                    BaseUserResolver baseUserResolver = (BaseUserResolver) iterator.next();
                    if (baseUserResolver.getId().equals(currentCase.getCaseObject().getUserResolver().getId())) {
                        iterator.remove();
                        break;
                    }

                }
            }
        }
    }

    private void initUserResolvers(EditCaseView currentCase) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", true);
        if (currentCase.getCaseObject().getSource() != null) {
            filters.put("source", currentCase.getCaseObject().getSource());
        }
        List<BaseUserResolver> userResolvers = teamService.findUserResolvers(currentCase.getSegment().getTeam(),
                filters);
        currentCase.setUserResolverOptions(userResolvers);
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.ASSIGN);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.USER_RESOLVER, assignAction.getSelectedUserResolver().getId());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
