package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
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
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.model.SendResponseActionView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.TeamService;

@VariableResolver(DelegatingVariableResolver.class)
public class SendResponseActionViewModel implements Serializable {

    private static final long serialVersionUID = 6188170388292392609L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private TeamService teamService;

    private String caseId;

    private List<BaseUserResolver> userResolverOptions;

    private SendResponseActionView sendResponseAction;

    private boolean chooseUserResolver;

    @Init
    public void init(@QueryParam("case") String caseId) {
        sendResponseAction = new SendResponseActionView();
        this.caseId = caseId;
    }

    public List<BaseUserResolver> getUserResolverOptions() {
        return userResolverOptions;
    }

    public void setUserResolverOptions(List<BaseUserResolver> userResolverOptions) {
        this.userResolverOptions = userResolverOptions;
    }

    public void setSendResponseAction(SendResponseActionView sendResponseAction) {
        this.sendResponseAction = sendResponseAction;
    }

    public SendResponseActionView getSendResponseAction() {
        return sendResponseAction;
    }

    public boolean isChooseUserResolver() {
        return chooseUserResolver;
    }

    @GlobalCommand
    @NotifyChange({ "sendResponseAction", "chooseUserResolver", "userResolverOptions" })
    public void show(@BindingParam("currentCase") Case currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.REPLY.equals(action)) {
            this.sendResponseAction = new SendResponseActionView();
            chooseUserResolver = currentCase.getUserResolver() == null;
            if (chooseUserResolver && userResolverOptions == null) {
                initUserResolvers(currentCase);
                this.sendResponseAction.setSelectedUserResolver(userResolverOptions.get(0));
            } else {
                this.sendResponseAction.setSelectedUserResolver(currentCase.getUserResolver());
            }
        }
    }

    private void initUserResolvers(Case currentCase) {
        Team team = teamService.findOne(currentCase.getTeamId());
        this.userResolverOptions = team.getUserResolvers();
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.REPLY);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.TEXT, sendResponseAction.getText());
        if (chooseUserResolver) {
            parameters.put(ActionParameter.USER_RESOLVER, sendResponseAction.getSelectedUserResolver().getId());
        }
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
