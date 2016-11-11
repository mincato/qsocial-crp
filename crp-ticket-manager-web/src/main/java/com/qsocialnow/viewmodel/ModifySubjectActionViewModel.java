package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class ModifySubjectActionViewModel implements Serializable {

    private static final long serialVersionUID = 5586253455623408477L;

    @WireVariable
    private CaseService caseService;

    private String caseId;

    @Init
    public void init(@QueryParam("case") String caseId) {
        this.caseId = caseId;
    }

    @GlobalCommand
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.MODIFY_SUBJECT.equals(action)) {
            Map<String, Object> arg = new HashMap<String, Object>();
            arg.put("subject", currentCase.getCaseObject().getSubject().getId());
            Executions.createComponents("/pages/cases/actions/edit-subject.zul", null, arg);
        }
    }

    @GlobalCommand
    public void modifySubject(@BindingParam("subject") Subject subject) {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.MODIFY_SUBJECT);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.SUBJECT, new GsonBuilder().create().toJson(subject));
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
