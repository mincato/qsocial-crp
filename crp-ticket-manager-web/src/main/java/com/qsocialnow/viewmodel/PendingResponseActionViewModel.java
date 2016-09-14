package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class PendingResponseActionViewModel implements Serializable {

    private static final long serialVersionUID = -6917261915296881303L;

    @WireVariable
    private CaseService caseService;

    private String caseId;

    @Init
    public void init(@QueryParam("case") String caseId) {
        this.caseId = caseId;
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.PENDING_RESPONSE);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);
        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
