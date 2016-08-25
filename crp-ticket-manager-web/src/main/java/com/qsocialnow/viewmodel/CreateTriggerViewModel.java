package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.services.DomainService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    private Trigger currentTrigger;

    private String domain;

    public Trigger getCurrentTrigger() {
        return currentTrigger;
    }

    @GlobalCommand
    @NotifyChange({ "currentTrigger" })
    public void addSegment(@BindingParam("segment") Segment segment) {
        currentTrigger.getSegments().add(segment);
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
    }

    @Init
    public void init(@QueryParam("domain") String domain) {
        this.domain = domain;
        currentTrigger = new Trigger();
        this.currentTrigger.setSegments(new ArrayList<>());
    }

    @Command
    @NotifyChange("currentTrigger")
    public void save() {
        domainService.createTrigger(domain, currentTrigger);
        Clients.showNotification(Labels.getLabel("trigger.create.notification.success"));
    }

}
