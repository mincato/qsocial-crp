package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.handler.NotificationHandler;
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
        PageResponse<DomainListView> pageResponse = domainService.findAll();
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
    public void onSelectDomain(@BindingParam("domain") DomainListView domain) {
        PageResponse<TriggerListView> pageResponse = triggerService.findAll(domain.getId(), 0, -1, null);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.triggers = new ArrayList<TriggerListView>();
            this.triggers.addAll(pageResponse.getItems());
        }
    }

    @Command
    @NotifyChange("segments")
    public void onSelectTrigger(@BindingParam("domain") DomainListView domain,
            @BindingParam("trigger") TriggerListView trigger) {
        this.segments = triggerService.findSegments(domain.getId(), trigger.getId());
    }

    @Command
    @NotifyChange("currentCase")
    public void save() {
        Case newCase = currentCase.getNewCase();
        newCase.setDomainId(currentCase.getSelectedDomain().getId());
        newCase.setTriggerId(currentCase.getSelectedTrigger().getId());
        newCase.setSegmentId(currentCase.getSelectedSegment().getId());

        if (currentCase.getSelectedSegment() != null)
            newCase.setTeamId(currentCase.getSelectedSegment().getTeamId());

        currentCase.setNewCase(caseService.create(newCase));

        NotificationHandler.addNotification(Labels.getLabel("cases.create.notification.success",
                new String[] { currentCase.getNewCase().getTitle() }));
        Executions.getCurrent().sendRedirect("/pages/cases/list/index.zul");
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

}