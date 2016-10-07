package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.model.CaseView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.TriggerService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateCaseViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private TriggerService triggerService;

    private CaseView currentCase;

    private DomainListView selectedDomain;

    private TriggerListView selectedTrigger;

    private SegmentListView selectedSegment;

    private List<DomainListView> domains = new ArrayList<DomainListView>();

    private List<TriggerListView> triggers;

    private List<SegmentListView> segments;

    public CaseView getCurrentCase() {
        return currentCase;
    }

    @Init
    public void init() {
        initDomain();
        initCase();
    }

    private void initDomain() {
        PageResponse<DomainListView> pageResponse = domainService.findAll(0, -1);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.domains.addAll(pageResponse.getItems());
        }
    }

    private void initCase() {
        currentCase = new CaseView();
        currentCase.setNewCase(Case.getNewCase());
    }

    @Command
    @NotifyChange("triggers")
    public void onSelectDomain() {
        PageResponse<TriggerListView> pageResponse = triggerService.findAll(this.selectedDomain.getId(), 0, -1, null);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.triggers = new ArrayList<TriggerListView>();
            this.triggers.addAll(pageResponse.getItems());
        }
    }

    @Command
    @NotifyChange("segments")
    public void onSelectTrigger() {
        this.segments = triggerService.findSegments(this.selectedDomain.getId(), this.selectedTrigger.getId());
    }

    @Command
    @NotifyChange("currentCase")
    public void save() {
        Case newCase = currentCase.getNewCase();
        newCase.setDomainId(selectedDomain.getId());
        newCase.setTriggerId(selectedTrigger.getId());
        newCase.setSegmentId(selectedSegment.getId());
        currentCase.setNewCase(caseService.create(newCase));
        Clients.showNotification(Labels.getLabel("cases.create.notification.success", new String[] { currentCase
                .getNewCase().getId() }));
        initCase();
    }

    @Command
    @NotifyChange({ "currentCase" })
    public void clear() {
        initCase();
    }

    public List<DomainListView> getDomains() {
        return domains;
    }

    public List<TriggerListView> getTriggers() {
        return triggers;
    }

    public List<SegmentListView> getSegments() {
        return segments;
    }

    public DomainListView getSelectedDomain() {
        return selectedDomain;
    }

    public void setSelectedDomain(DomainListView selectedDomain) {
        this.selectedDomain = selectedDomain;
    }

    public TriggerListView getSelectedTrigger() {
        return selectedTrigger;
    }

    public void setSelectedTrigger(TriggerListView selectedTrigger) {
        this.selectedTrigger = selectedTrigger;
    }

    public void setSelectedSegment(SegmentListView selectedSegment) {
        this.selectedSegment = selectedSegment;
    }

    public SegmentListView getSelectedSegment() {
        return selectedSegment;
    }

}