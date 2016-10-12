package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.model.ChangeSubjectActionView;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
public class ChangeSubjectActionViewModel implements Serializable {

    private static final long serialVersionUID = -4036545424903222177L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private SubjectService subjectService;

    private String caseId;

    private ChangeSubjectActionView changeSubjectAction;

    private ChangeSubjectActionView fxChangeSubjectAction;

    private String source;

    @Init
    public void init(@QueryParam("case") String caseId) {
        changeSubjectAction = new ChangeSubjectActionView();
        this.caseId = caseId;
    }

    public ChangeSubjectActionView getChangeSubjectAction() {
        return changeSubjectAction;
    }

    public void setChangeSubjectAction(ChangeSubjectActionView changeSubjectAction) {
        this.changeSubjectAction = changeSubjectAction;
    }

    @GlobalCommand
    @NotifyChange({ "changeSubjectAction", "subjectOptions" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.CHANGE_SUBJECT.equals(action)) {
            this.fxChangeSubjectAction = null;
            this.changeSubjectAction = new ChangeSubjectActionView();
            if (currentCase.getCaseObject().getSubject() != null) {
                SubjectListView subjectListView = new SubjectListView();
                subjectListView.setId(currentCase.getCaseObject().getSubject().getId());
                subjectListView.setName(currentCase.getCaseObject().getSubject().getName());
                subjectListView.setLastName(currentCase.getCaseObject().getSubject().getLastName());
                subjectListView.setIdentifier(currentCase.getCaseObject().getSubject().getIdentifier());
                this.changeSubjectAction.setSelectedSubject(subjectListView);
            }
            this.source = currentCase.getCaseObject().getSource() != null ? currentCase.getCaseObject().getSource()
                    .toString() : null;
        }
    }

    @Command
    public void editSubject(@BindingParam("fxChangeSubjectAction") ChangeSubjectActionView fxChangeSubjectAction) {
        this.fxChangeSubjectAction = fxChangeSubjectAction;
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        Executions.createComponents("/pages/cases/actions/choose-subject.zul", null, params);
    }

    @GlobalCommand
    public void changeSubject(@BindingParam("subject") SubjectListView subject) {
        this.fxChangeSubjectAction.setSelectedSubject(subject);
        BindUtils.postNotifyChange(null, null, fxChangeSubjectAction, "selectedSubject");
        this.fxChangeSubjectAction = null;
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.CHANGE_SUBJECT);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.SUBJECT, changeSubjectAction.getSelectedSubject().getId());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
