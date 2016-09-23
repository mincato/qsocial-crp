package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.TriggerService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private DomainService domainService;

    private Trigger currentTrigger;

    private Trigger fxTrigger;

    private String domain;

    private DomainView currentDomain;

    private List<Status> statusOptions;

    public Trigger getCurrentTrigger() {
        return currentTrigger;
    }

    public List<Status> getStatusOptions() {
        return statusOptions;
    }

    @GlobalCommand
    public void addSegment(@BindingParam("segment") Segment segment) {
        this.fxTrigger.getSegments().add(segment);
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
        BindUtils.postNotifyChange(null, null, this.fxTrigger, "segments");
        this.fxTrigger = null;
    }

    @GlobalCommand
    public void updateSegment() {
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
        BindUtils.postNotifyChange(null, null, this.fxTrigger, "segments");
        this.fxTrigger = null;
    }

    @Command
    public void createSegment(@BindingParam("fxTrigger") Trigger fxTrigger) {
        this.fxTrigger = fxTrigger;
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain.getDomain());
        BindUtils.postGlobalCommand(null, null, "initSegment", args);
    }

    @Command
    public void editSegment(@BindingParam("fxTrigger") Trigger fxTrigger, @BindingParam("segment") Segment segment) {
        this.fxTrigger = fxTrigger;
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain.getDomain());
        args.put("segment", segment);
        BindUtils.postGlobalCommand(null, null, "editSegment", args);
    }

    @Command
    public void removeSegment(@BindingParam("segment") Segment segment, @BindingParam("fxTrigger") Trigger fxTrigger) {
        fxTrigger.getSegments().remove(segment);
        BindUtils.postNotifyChange(null, null, fxTrigger, "segments");
    }

    @Init
    public void init(@QueryParam("domain") String domain) {
        this.domain = domain;
        this.currentDomain = new DomainView();
        this.currentDomain.setDomain(domainService.findOne(this.domain));
        this.currentTrigger = new Trigger();
        this.currentTrigger.setSegments(new ArrayList<>());
        this.statusOptions = Arrays.asList(Status.values());
    }

    @Command
    @NotifyChange("currentTrigger")
    public void save() {
        triggerService.create(domain, currentTrigger);
        Clients.showNotification(Labels.getLabel("trigger.create.notification.success"));
        currentTrigger = new Trigger();
        currentTrigger.setSegments(new ArrayList<>());
    }

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

}
