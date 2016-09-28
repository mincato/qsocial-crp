package com.qsocialnow.model;

import java.util.List;

import javax.validation.Valid;

import com.qsocialnow.common.model.config.Trigger;

public class TriggerView {

    @Valid
    private Trigger trigger;

    private List<TriggerResolutionView> resolutions;

    private List<TriggerCaseCategorySetView> caseCategorySets;

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public List<TriggerResolutionView> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<TriggerResolutionView> resolutions) {
        this.resolutions = resolutions;
    }

    public List<TriggerCaseCategorySetView> getCaseCategorySets() {
        return caseCategorySets;
    }

    public void setCaseCategorySets(List<TriggerCaseCategorySetView> caseCategorySets) {
        this.caseCategorySets = caseCategorySets;
    }

}
