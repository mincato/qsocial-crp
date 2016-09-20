package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class CloseActionViewModel implements Serializable {

    private static final long serialVersionUID = -4969736421040158073L;

    @WireVariable
    private CaseService caseService;

    private String comment;

    private String caseId;

    @Init
    public void init(@QueryParam("case") String caseId) {
        comment = null;
        this.caseId = caseId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @GlobalCommand
    @NotifyChange({ "comment" })
    public void show() {
        this.comment = null;
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.CLOSE);
        if (StringUtils.isNotBlank(comment)) {
            Map<ActionParameter, Object> parameters = new HashMap<>();
            parameters.put(ActionParameter.COMMENT, comment);
            actionRequest.setParameters(parameters);
        }
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
        BindUtils.postGlobalCommand(null, null, "refreshRegistries", args);
        BindUtils.postGlobalCommand(null, null, "getAllowedActionsByCase", args);
    }

}
