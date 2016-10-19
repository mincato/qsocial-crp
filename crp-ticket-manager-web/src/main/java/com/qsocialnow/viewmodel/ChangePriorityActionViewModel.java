package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.qsocialnow.common.model.cases.Priority;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.model.ChangePriorityActionView;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class ChangePriorityActionViewModel implements Serializable {

    private static final long serialVersionUID = 1572871613274316174L;

    @WireVariable
    private CaseService caseService;

    private String caseId;

    private List<Priority> priorityOptions;

    private ChangePriorityActionView changePriorityAction;

    @Init
    public void init(@QueryParam("case") String caseId) {
        changePriorityAction = new ChangePriorityActionView();
        this.priorityOptions = Arrays.asList(Priority.values());
        this.caseId = caseId;
    }

    public List<Priority> getPriorityOptions() {
        return priorityOptions;
    }

    public void setPriorityOptions(List<Priority> priorityOptions) {
        this.priorityOptions = priorityOptions;
    }

    public ChangePriorityActionView getChangePriorityAction() {
        return changePriorityAction;
    }

    public void setChangePriorityAction(ChangePriorityActionView changePriorityAction) {
        this.changePriorityAction = changePriorityAction;
    }

    @GlobalCommand
    @NotifyChange({ "changePriorityAction", "priorityOptions" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.CHANGE_PRIORITY.equals(action)) {
            this.changePriorityAction = new ChangePriorityActionView();
            this.priorityOptions = new ArrayList<>(Arrays.asList(Priority.values()));
            if (currentCase.getCaseObject().getPriority() != null) {
                for (Iterator<Priority> iterator = priorityOptions.iterator(); iterator.hasNext();) {
                    Priority priority = iterator.next();
                    if (priority.equals(currentCase.getCaseObject().getPriority())) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.CHANGE_PRIORITY);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.PRIORITY, changePriorityAction.getSelectedPriority());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
