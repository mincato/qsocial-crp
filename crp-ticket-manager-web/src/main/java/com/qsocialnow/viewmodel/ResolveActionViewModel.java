package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.model.ResolveActionView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class ResolveActionViewModel implements Serializable {

    private static final long serialVersionUID = -4340944629965909778L;

    @WireVariable
    private CaseService caseService;

    private String comment;

    private String caseId;

    private List<Resolution> resolutionOptions;

    private ResolveActionView resolveAction;

    @Init
    public void init(@QueryParam("case") String caseId) {
        comment = null;
        this.caseId = caseId;
        resolutionOptions = caseService.getAvailableResolutions(caseId);
        resolveAction = new ResolveActionView();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Resolution> getResolutionOptions() {
        return resolutionOptions;
    }

    public void setResolutionOptions(List<Resolution> resolutionOptions) {
        this.resolutionOptions = resolutionOptions;
    }

    public ResolveActionView getResolveAction() {
        return resolveAction;
    }

    public void setResolveAction(ResolveActionView resolveAction) {
        this.resolveAction = resolveAction;
    }

    @GlobalCommand
    @NotifyChange({ "comment", "resolveAction" })
    public void show() {
        this.comment = null;
        this.resolveAction = new ResolveActionView();
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.RESOLVE);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.RESOLUTION, resolveAction.getSelectedResolution().getId());
        if (StringUtils.isNotBlank(comment)) {
            parameters.put(ActionParameter.COMMENT, comment);
        }
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
