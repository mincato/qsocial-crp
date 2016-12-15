package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.model.SegmentView;
import com.qsocialnow.model.TriggerView;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateActionViewModel implements Serializable {

    private static final long serialVersionUID = 6384063108880208673L;

    private List<ActionType> actionOptions = new ArrayList<>();

    private ActionType selectedAction;

    private TriggerView trigger;

    private SegmentView segment;

    @WireVariable
    private UserSessionService userSessionService;

    @Init
    public void init() {
        this.actionOptions = new ArrayList<>(ActionType.automaticActions);
        if (!userSessionService.isOdatech()) {
            this.actionOptions.remove(ActionType.REPLY);
        }
    }

    @GlobalCommand
    public void initAction(@BindingParam("trigger") TriggerView trigger, @BindingParam("segment") SegmentView segment) {
        this.trigger = trigger;
        this.segment = segment;
    }

    @Command
    public void onSelectAction() {
        Map<String, Object> args = new HashMap<>();
        args.put("action", selectedAction);
        args.put("trigger", trigger);
        args.put("segment", segment);
        BindUtils.postGlobalCommand(null, null, "show", args);
    }

    @GlobalCommand
    @NotifyChange("selectedAction")
    public void saveActionCriteria(@BindingParam("actionCriteria") AutomaticActionCriteria actionCriteria) {
        Map<String, Object> args = new HashMap<>();
        args.put("actionCriteria", actionCriteria);
        BindUtils.postGlobalCommand(null, null, "addActionCriteria", args);
        selectedAction = null;
    }

    @GlobalCommand
    @NotifyChange("selectedAction")
    public void cancel() {
        selectedAction = null;
        BindUtils.postGlobalCommand(null, null, "goToCriteria", new HashMap<>());
    }

    public List<ActionType> getActionOptions() {
        return actionOptions;
    }

    public ActionType getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(ActionType selectedAction) {
        this.selectedAction = selectedAction;
    }

}
