package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.bind.annotation.ToServerCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.model.ListView;
import com.qsocialnow.model.TriggerResolutionView;
import com.qsocialnow.model.TriggerView;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.TriggerService;

@ToServerCommand("collapsetoggleResolutions")
@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private DomainService domainService;

    private TriggerView currentTrigger;

    private Trigger fxTrigger;

    private String domain;

    private DomainView currentDomain;

    private List<Status> statusOptions;

    private boolean editing;

    private boolean editingResolutions;

    private ListView<Resolution> resolutionListView;

    public TriggerView getCurrentTrigger() {
        return currentTrigger;
    }

    public List<Status> getStatusOptions() {
        return statusOptions;
    }

    public ListView<Resolution> getResolutionListView() {
        return resolutionListView;
    }

    public boolean isEditingResolutions() {
        return editingResolutions;
    }

    @Command("collapsetoggleResolutions")
    @NotifyChange("editingResolutions")
    public void toggleResolutions(@BindingParam("toggle") Boolean toggle) {
        this.editingResolutions = toggle;
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

    @Command
    @NotifyChange({ "resolutionListView" })
    public void addResolution(@BindingParam("fxTrigger") TriggerView trigger) {
        TriggerResolutionView resolution = new TriggerResolutionView();
        resolution.setEditingStatus(Boolean.TRUE);
        trigger.getResolutions().add(resolution);
        resolutionListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, trigger, "resolutions");
    }

    @Command
    @NotifyChange({ "resolutionListView" })
    public void confirmResolution(@BindingParam("resolution") TriggerResolutionView resolution,
            @BindingParam("fxTrigger") TriggerView trigger) {
        resolution.setEditingStatus(Boolean.FALSE);
        deleteResolutionFilteredList(resolutionListView, resolution.getResolution());
        resolutionListView.setEnabledAdd(true);
        BindUtils.postNotifyChange(null, null, trigger, "resolutions");

    }

    @Command
    @NotifyChange({ "resolutionListView" })
    public void removeResolution(@BindingParam("index") int idx, @BindingParam("fxTrigger") TriggerView trigger) {
        TriggerResolutionView deletedResolution = trigger.getResolutions().remove(idx);
        if (!deletedResolution.isEditingStatus()) {
            addResolutionFilteredList(resolutionListView, deletedResolution.getResolution());
        }
        resolutionListView.setEnabledAdd(true);
        for (TriggerResolutionView resolution : trigger.getResolutions()) {
            if (resolution.isEditingStatus()) {
                resolutionListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, trigger, "resolutions");

    }

    private void addResolutionFilteredList(ListView<Resolution> resolutionListView, Resolution resolution) {
        if (resolution == null) {
            return;
        }
        for (Resolution resolutionList : resolutionListView.getList()) {
            if (resolutionList.getId().equals(resolution.getId())) {
                resolutionListView.getFilteredList().add(resolutionList);
            }
        }
    }

    private void deleteResolutionFilteredList(ListView<Resolution> resolutionListView, Resolution resolution) {
        if (resolution == null) {
            return;
        }
        resolutionListView.getFilteredList().remove(resolution);
    }

    @Init
    public void init(@QueryParam("domain") String domain, @QueryParam("trigger") String triggerId) {
        this.domain = domain;
        currentDomain = new DomainView();
        currentDomain.setDomain(domainService.findOne(this.domain));
        initTrigger(domain, triggerId);
        initResolutionListView(currentTrigger.getResolutions(), currentDomain.getDomain().getResolutions());
        statusOptions = Arrays.asList(Status.values());
    }

    private void initTrigger(String domain, String triggerId) {
        currentTrigger = new TriggerView();
        if (triggerId != null) {
            editing = true;
            currentTrigger.setTrigger(triggerService.findOne(domain, triggerId));
            currentTrigger.setResolutions(currentTrigger.getTrigger().getResolutions().stream().map(resolution -> {
                TriggerResolutionView triggerResolutionView = new TriggerResolutionView();
                triggerResolutionView.setResolution(resolution);
                return triggerResolutionView;
            }).collect(Collectors.toList()));
        } else {
            editing = false;
            currentTrigger.setTrigger(new Trigger());
            currentTrigger.getTrigger().setSegments(new ArrayList<>());
            currentTrigger.setResolutions(new ArrayList<>());
        }
    }

    private void initResolutionListView(List<TriggerResolutionView> triggerResolutions,
            List<Resolution> domainResolutions) {
        resolutionListView = new ListView<Resolution>();
        resolutionListView.setList(domainResolutions);
        resolutionListView.setFilteredList(new ArrayList<Resolution>());
        resolutionListView.getFilteredList().addAll(resolutionListView.getList().stream().filter(resolution -> {
            boolean found = false;
            for (TriggerResolutionView triggerResolutionView : triggerResolutions) {
                if (triggerResolutionView.getResolution().getId().equals(resolution.getId()))
                    found = true;
            }
            return !found;
        }).collect(Collectors.toList()));
    }

    @Command
    @NotifyChange("currentTrigger")
    public void save() {
        currentTrigger.getTrigger().setResolutions(
                currentTrigger.getResolutions().stream().map(TriggerResolutionView::getResolution)
                        .collect(Collectors.toList()));
        if (editing) {
            this.currentTrigger.setTrigger(triggerService.update(this.currentDomain.getDomain().getId(),
                    currentTrigger.getTrigger()));
            Clients.showNotification(Labels.getLabel("trigger.edit.notification.success"));
        } else {
            triggerService.create(domain, currentTrigger.getTrigger());
            Clients.showNotification(Labels.getLabel("trigger.create.notification.success"));
            currentTrigger.setTrigger(new Trigger());
            currentTrigger.getTrigger().setSegments(new ArrayList<>());
            currentTrigger.getTrigger().setResolutions(new ArrayList<>());
        }
    }

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

    public ListView<Resolution> getCaseCategoryListView() {
        return resolutionListView;
    }

}
