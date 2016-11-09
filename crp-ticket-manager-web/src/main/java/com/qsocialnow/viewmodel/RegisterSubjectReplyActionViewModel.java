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
import com.qsocialnow.model.RegisterSubjectReplyActionView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class RegisterSubjectReplyActionViewModel implements Serializable {

    private static final long serialVersionUID = 2380735423248847016L;

    @WireVariable
    private CaseService caseService;

    private RegisterSubjectReplyActionView registerSubjectReplyAction;

    private String caseId;

    @Init
    public void init(@QueryParam("case") String caseId) {
        registerSubjectReplyAction = new RegisterSubjectReplyActionView();
        this.caseId = caseId;
    }

    public RegisterSubjectReplyActionView getRegisterSubjectReplyAction() {
        return registerSubjectReplyAction;
    }

    public void setRegisterSubjectReplyAction(RegisterSubjectReplyActionView registerSubjectReplyAction) {
        this.registerSubjectReplyAction = registerSubjectReplyAction;
    }

    @GlobalCommand
    @NotifyChange({ "registerSubjectReplyAction" })
    public void show() {
        this.registerSubjectReplyAction = new RegisterSubjectReplyActionView();
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.REGISTER_SUBJECT_REPLY);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.TEXT, registerSubjectReplyAction.getText());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
