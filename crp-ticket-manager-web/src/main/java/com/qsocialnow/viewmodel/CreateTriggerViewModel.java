package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.services.TriggerService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private TriggerService triggerService;

    private Trigger currentTrigger;

    public Trigger getCurrentTrigger() {
        return currentTrigger;
    }

    @Init
    public void init() {
        currentTrigger = new Trigger();
    }

    @Command
    @NotifyChange("currentTrigger")
    public void save() {
    }

    @Command
    @NotifyChange({ "currentTrigger" })
    public void clear() {
    }

}
