package com.qsocialnow.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.cases.Case;
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

}
