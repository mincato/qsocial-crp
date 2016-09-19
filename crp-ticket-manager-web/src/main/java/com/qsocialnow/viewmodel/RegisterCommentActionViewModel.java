package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
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
import com.qsocialnow.model.RegisterCommentActionView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class RegisterCommentActionViewModel implements Serializable {

    private static final long serialVersionUID = -9132377448978065216L;

    @WireVariable
    private CaseService caseService;

    private RegisterCommentActionView registerCommentAction;

    private String caseId;

    @Init
    public void init(@QueryParam("case") String caseId) {
        registerCommentAction = new RegisterCommentActionView();
        this.caseId = caseId;
    }

    public RegisterCommentActionView getRegisterCommentAction() {
        return registerCommentAction;
    }

    public void setRegisterCommentAction(RegisterCommentActionView registerCommentAction) {
        this.registerCommentAction = registerCommentAction;
    }

    @GlobalCommand
    @NotifyChange({ "registerCommentAction" })
    public void show() {
        this.registerCommentAction = new RegisterCommentActionView();
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.REGISTER_COMMENT);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.COMMENT, registerCommentAction.getComment());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
        BindUtils.postGlobalCommand(null, null, "refreshRegistries", args);
    }

}
