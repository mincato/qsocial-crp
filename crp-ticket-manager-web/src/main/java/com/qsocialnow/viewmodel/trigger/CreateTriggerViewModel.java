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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.model.ListView;
import com.qsocialnow.model.TriggerCaseCategorySetView;
import com.qsocialnow.model.TriggerResolutionView;
import com.qsocialnow.model.TriggerSubjectCategorySetView;
import com.qsocialnow.model.TriggerView;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.TriggerService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private TriggerView currentTrigger;

    private Trigger fxTrigger;

    private String domain;

    private DomainView currentDomain;

    private List<Status> statusOptions;

    private boolean editing;

    private ListView<Resolution> resolutionListView;

    private ListView<CaseCategorySet> caseCategorySetListView;

    private ListView<SubjectCategorySet> subjectCategorySetListView;

    public TriggerView getCurrentTrigger() {
        return currentTrigger;
    }

    public List<Status> getStatusOptions() {
        return statusOptions;
    }

    public ListView<Resolution> getResolutionListView() {
        return resolutionListView;
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
    public void createSegment(@BindingParam("fxTrigger") TriggerView fxTrigger) {
        this.fxTrigger = fxTrigger.getTrigger();
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain.getDomain());
        args.put("trigger", fxTrigger);
        BindUtils.postGlobalCommand(null, null, "initSegment", args);
    }

    @Command
    public void editSegment(@BindingParam("fxTrigger") TriggerView fxTrigger, @BindingParam("segment") Segment segment) {
        this.fxTrigger = fxTrigger.getTrigger();
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain.getDomain());
        args.put("trigger", fxTrigger);
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
        initCaseCategorySetListView(currentTrigger.getCaseCategorySets());
        initSubjectCategorySetListView(currentTrigger.getSubjectCategorySets());
        statusOptions = Arrays.asList(Status.values());
    }

    private void initCaseCategorySetListView(List<TriggerCaseCategorySetView> triggerCaseCategorySets) {
        caseCategorySetListView = new ListView<CaseCategorySet>();
        caseCategorySetListView.setList(caseCategorySetService.findAll());
        caseCategorySetListView.setFilteredList(new ArrayList<CaseCategorySet>());
        if (triggerCaseCategorySets != null) {
            caseCategorySetListView.getFilteredList().addAll(
                    caseCategorySetListView
                            .getList()
                            .stream()
                            .filter(caseCategorySet -> {
                                boolean found = false;
                                for (TriggerCaseCategorySetView triggerCaseCategorySetView : triggerCaseCategorySets) {
                                    if (triggerCaseCategorySetView.getCaseCategorySet().getId()
                                            .equals(caseCategorySet.getId()))
                                        found = true;
                                }
                                return !found;
                            }).collect(Collectors.toList()));
        } else {
            caseCategorySetListView.getFilteredList().addAll(caseCategorySetListView.getList());
        }
    }

    private void initSubjectCategorySetListView(List<TriggerSubjectCategorySetView> triggerSubjectCategorySets) {
        subjectCategorySetListView = new ListView<SubjectCategorySet>();
        subjectCategorySetListView.setList(subjectCategorySetService.findAll());
        subjectCategorySetListView.setFilteredList(new ArrayList<SubjectCategorySet>());
        if (triggerSubjectCategorySets != null) {
            subjectCategorySetListView
                    .getFilteredList()
                    .addAll(subjectCategorySetListView
                            .getList()
                            .stream()
                            .filter(subjectCategorySet -> {
                                boolean found = false;
                                for (TriggerSubjectCategorySetView triggerSubjectCategorySetView : triggerSubjectCategorySets) {
                                    if (triggerSubjectCategorySetView.getSubjectCategorySet().getId()
                                            .equals(subjectCategorySet.getId()))
                                        found = true;
                                }
                                return !found;
                            }).collect(Collectors.toList()));
        } else {
            subjectCategorySetListView.getFilteredList().addAll(subjectCategorySetListView.getList());
        }
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
            currentTrigger.setCaseCategorySets(currentTrigger.getTrigger().getCaseCategoriesSetIds().stream()
                    .map(caseCategorySet -> {
                        TriggerCaseCategorySetView triggerCaseCategorySetView = new TriggerCaseCategorySetView();
                        triggerCaseCategorySetView.setCaseCategorySet(caseCategorySetService.findOne(caseCategorySet));
                        return triggerCaseCategorySetView;
                    }).collect(Collectors.toList()));
            if (currentTrigger.getTrigger().getSubjectCategoriesSetIds() != null) {
                currentTrigger
                        .setSubjectCategorySets(currentTrigger
                                .getTrigger()
                                .getSubjectCategoriesSetIds()
                                .stream()
                                .map(subjectCategorySet -> {
                                    TriggerSubjectCategorySetView triggerSubjectCategorySetView = new TriggerSubjectCategorySetView();
                                    triggerSubjectCategorySetView.setSubjectCategorySet(subjectCategorySetService
                                            .findOne(subjectCategorySet));
                                    return triggerSubjectCategorySetView;
                                }).collect(Collectors.toList()));
            } else {
                currentTrigger.setSubjectCategorySets(new ArrayList<>());
            }
        } else {
            editing = false;
            currentTrigger.setTrigger(new Trigger());
            currentTrigger.getTrigger().setSegments(new ArrayList<>());
            currentTrigger.setResolutions(new ArrayList<>());
            currentTrigger.setCaseCategorySets(new ArrayList<>());
            currentTrigger.setSubjectCategorySets(new ArrayList<>());
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
        currentTrigger.getTrigger().setCaseCategoriesSetIds(currentTrigger.getCaseCategorySets().stream().map(view -> {
            return view.getCaseCategorySet().getId();
        }).collect(Collectors.toList()));
        currentTrigger.getTrigger().setSubjectCategoriesSetIds(
                currentTrigger.getSubjectCategorySets().stream().map(view -> {
                    return view.getSubjectCategorySet().getId();
                }).collect(Collectors.toList()));
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
            currentTrigger.getTrigger().setCaseCategoriesSetIds(new ArrayList<>());
            currentTrigger.getTrigger().setSubjectCategoriesSetIds(new ArrayList<>());
            initCaseCategorySetListView(null);
            initSubjectCategorySetListView(null);
        }
    }

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

    public ListView<CaseCategorySet> getCaseCategorySetListView() {
        return caseCategorySetListView;
    }

    @Command
    @NotifyChange({ "caseCategorySetListView" })
    public void addCaseCategorySet(@BindingParam("fxTrigger") TriggerView trigger) {
        TriggerCaseCategorySetView caseCategory = new TriggerCaseCategorySetView();
        caseCategory.setEditingStatus(Boolean.TRUE);
        trigger.getCaseCategorySets().add(caseCategory);
        caseCategorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, trigger, "caseCategorySets");
    }

    @Command
    @NotifyChange({ "caseCategorySetListView" })
    public void confirmCaseCategorySet(@BindingParam("caseCategorySet") TriggerCaseCategorySetView caseCategorySet,
            @BindingParam("fxTrigger") TriggerView trigger) {
        caseCategorySet.setEditingStatus(Boolean.FALSE);
        deleteCaseCategorySetFilteredList(caseCategorySetListView, caseCategorySet.getCaseCategorySet());
        caseCategorySetListView.setEnabledAdd(true);
        BindUtils.postNotifyChange(null, null, trigger, "caseCategorySets");

    }

    @Command
    @NotifyChange({ "caseCategorySetListView" })
    public void removeCaseCategorySet(@BindingParam("index") int idx, @BindingParam("fxTrigger") TriggerView trigger) {
        TriggerCaseCategorySetView deletedCaseCategorySet = trigger.getCaseCategorySets().remove(idx);
        if (!deletedCaseCategorySet.isEditingStatus()) {
            addCaseCategorySetFilteredList(caseCategorySetListView, deletedCaseCategorySet.getCaseCategorySet());
        }
        caseCategorySetListView.setEnabledAdd(true);
        for (TriggerCaseCategorySetView caseCategorySet : trigger.getCaseCategorySets()) {
            if (caseCategorySet.isEditingStatus()) {
                caseCategorySetListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, trigger, "caseCategorySets");

    }

    private void addCaseCategorySetFilteredList(ListView<CaseCategorySet> caseCategorySetListView,
            CaseCategorySet caseCategorySet) {
        if (caseCategorySet == null) {
            return;
        }
        for (CaseCategorySet caseCategorySetList : caseCategorySetListView.getList()) {
            if (caseCategorySetList.getId().equals(caseCategorySet.getId())) {
                caseCategorySetListView.getFilteredList().add(caseCategorySetList);
            }
        }
    }

    private void deleteCaseCategorySetFilteredList(ListView<CaseCategorySet> caseCategorySetListView,
            CaseCategorySet caseCategorySet) {
        if (caseCategorySet == null) {
            return;
        }
        caseCategorySetListView.getFilteredList().remove(caseCategorySet);
    }

    public ListView<SubjectCategorySet> getSubjectCategorySetListView() {
        return subjectCategorySetListView;
    }

    @Command
    @NotifyChange({ "subjectCategorySetListView" })
    public void addSubjectCategorySet(@BindingParam("fxTrigger") TriggerView trigger) {
        TriggerSubjectCategorySetView subjectCategory = new TriggerSubjectCategorySetView();
        subjectCategory.setEditingStatus(Boolean.TRUE);
        trigger.getSubjectCategorySets().add(subjectCategory);
        subjectCategorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, trigger, "subjectCategorySets");
    }

    @Command
    @NotifyChange({ "subjectCategorySetListView" })
    public void confirmSubjectCategorySet(
            @BindingParam("subjectCategorySet") TriggerSubjectCategorySetView subjectCategorySet,
            @BindingParam("fxTrigger") TriggerView trigger) {
        subjectCategorySet.setEditingStatus(Boolean.FALSE);
        deleteSubjectCategorySetFilteredList(subjectCategorySetListView, subjectCategorySet.getSubjectCategorySet());
        subjectCategorySetListView.setEnabledAdd(true);
        BindUtils.postNotifyChange(null, null, trigger, "subjectCategorySets");

    }

    @Command
    @NotifyChange({ "subjectCategorySetListView" })
    public void removeSubjectCategorySet(@BindingParam("index") int idx, @BindingParam("fxTrigger") TriggerView trigger) {
        TriggerSubjectCategorySetView deletedSubjectCategorySet = trigger.getSubjectCategorySets().remove(idx);
        if (!deletedSubjectCategorySet.isEditingStatus()) {
            addSubjectCategorySetFilteredList(subjectCategorySetListView,
                    deletedSubjectCategorySet.getSubjectCategorySet());
        }
        subjectCategorySetListView.setEnabledAdd(true);
        for (TriggerSubjectCategorySetView subjectCategorySet : trigger.getSubjectCategorySets()) {
            if (subjectCategorySet.isEditingStatus()) {
                subjectCategorySetListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, trigger, "subjectCategorySets");

    }

    private void addSubjectCategorySetFilteredList(ListView<SubjectCategorySet> subjectCategorySetListView,
            SubjectCategorySet subjectCategorySet) {
        if (subjectCategorySet == null) {
            return;
        }
        for (SubjectCategorySet subjectCategorySetList : subjectCategorySetListView.getList()) {
            if (subjectCategorySetList.getId().equals(subjectCategorySet.getId())) {
                subjectCategorySetListView.getFilteredList().add(subjectCategorySetList);
            }
        }
    }

    private void deleteSubjectCategorySetFilteredList(ListView<SubjectCategorySet> subjectCategorySetListView,
            SubjectCategorySet subjectCategorySet) {
        if (subjectCategorySet == null) {
            return;
        }
        subjectCategorySetListView.getFilteredList().remove(subjectCategorySet);
    }

}
