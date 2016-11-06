package com.qsocialnow.viewmodel.factory;

import java.util.List;
import java.util.Set;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseCategorySetService;

public class EditCaseCaseCategoriesFactory extends EditCaseCategoriesFactory<CaseCategorySet, CaseCategory> {

    private CaseCategorySetService caseCategorySetService;

    @Override
    protected void setTriggerSetsInCurrentCase(EditCaseView currentCase, List<CaseCategorySet> sets) {
        currentCase.setTriggerCategories(sets);
    }

    @Override
    protected List<CaseCategorySet> getTriggerSetsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getTriggerCategories();
    }

    @Override
    protected void setAllTriggerSetsInCurrentCase(EditCaseView currentCase, List<CaseCategorySet> sets) {
        currentCase.setAllTriggerCategories(sets);
    }

    @Override
    protected List<CaseCategorySet> getAllTriggerSetsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getAllTriggerCategories();
    }

    @Override
    protected List<CaseCategorySet> getTriggerSetsFromTriggerService(String domainId, String triggerId) {
        return triggerService.findCategories(domainId, triggerId);
    }

    @Override
    protected Set<String> getSetIdsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getCaseObject().getCaseCategoriesSet();
    }

    @Override
    protected Set<String> getCategoriesIdsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getCaseObject().getCaseCategories();
    }

    @Override
    protected List<CaseCategory> getCategoriesFromSet(CaseCategorySet set) {
        return set.getCategories();
    }

    @Override
    protected List<CaseCategorySet> getDissasociatedSetsFromService(List<String> ids) {
        return caseCategorySetService.findByIds(ids);
    }

    @Override
    protected List<CaseCategory> getDissasociatedCategoriesFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getDisassociatedCategories();
    }

    @Override
    protected void setDissasociatedCategoriesInCurrentCase(EditCaseView currentCase, List<CaseCategory> categories) {
        currentCase.setDisassociatedCategories(categories);
    }

    @Override
    protected void setSetsInCurrentCase(EditCaseView currentCase, List<CaseCategorySet> sets) {
        currentCase.setCaseCategoriesSet(sets);
    }

    @Override
    protected void setCategoriesInCurrentCase(EditCaseView currentCase, List<CaseCategory> categories) {
        currentCase.setCaseCategories(categories);
    }

    public void setCaseCategorySetService(CaseCategorySetService caseCategorySetService) {
        this.caseCategorySetService = caseCategorySetService;
    }

    public CaseCategorySetService getCaseCategorySetService() {
        return caseCategorySetService;
    }
}
