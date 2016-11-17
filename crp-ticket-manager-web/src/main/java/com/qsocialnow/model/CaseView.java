package com.qsocialnow.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;

public class CaseView {

    @Valid
    private Case newCase;

    @NotNull(message = "app.field.empty.validation")
    private DomainListView selectedDomain;

    @NotNull(message = "app.field.empty.validation")
    private TriggerListView selectedTrigger;

    @NotNull(message = "app.field.empty.validation")
    private SegmentListView selectedSegment;

    private AdminUnit adminUnit;

    @NotNull(message = "app.subject.null.validation")
    private Subject selectedSubject;

    public CaseView() {
    }

    public Case getNewCase() {
        return newCase;
    }

    public void setNewCase(Case newCase) {
        this.newCase = newCase;
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

    public SegmentListView getSelectedSegment() {
        return selectedSegment;
    }

    public void setSelectedSegment(SegmentListView selectedSegment) {
        this.selectedSegment = selectedSegment;
    }

    public AdminUnit getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnit adminUnit) {
        this.adminUnit = adminUnit;
    }

    public Subject getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

}