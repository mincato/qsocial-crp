package com.qsocialnow.viewmodel.factory;

import java.util.List;
import java.util.Set;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.SubjectCategorySetService;

public class EditCaseSubjectCategoriesFactory extends EditCaseCategoriesFactory<SubjectCategorySet, SubjectCategory> {

    private SubjectCategorySetService subjectCategorySetService;

    @Override
    protected List<SubjectCategorySet> getTriggerSetsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getTriggerSubjectCategories();
    }

    @Override
    protected List<SubjectCategorySet> getTriggerSetsFromTriggerService(String domainId, String triggerId) {
        return triggerService.findSubjectCategories(domainId, triggerId);
    }

    @Override
    protected void setAllTriggerSetsInCurrentCase(EditCaseView currentCase, List<SubjectCategorySet> sets) {
        currentCase.setAllTriggerSubjectCategories(sets);
    }

    @Override
    protected void setTriggerSetsInCurrentCase(EditCaseView currentCase, List<SubjectCategorySet> sets) {
        currentCase.setTriggerSubjectCategories(sets);
    }

    @Override
    protected Set<String> getSetIdsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getCaseObject().getSubject().getSubjectCategorySet();
    }

    @Override
    protected void setDissasociatedCategoriesInCurrentCase(EditCaseView currentCase, List<SubjectCategory> categories) {
        currentCase.setDisassociatedSubjectCategories(categories);
    }

    @Override
    protected List<SubjectCategorySet> getDissasociatedSetsFromService(List<String> ids) {
        return subjectCategorySetService.findByIds(ids);
    }

    @Override
    protected List<SubjectCategory> getCategoriesFromSet(SubjectCategorySet set) {
        return set.getCategories();
    }

    @Override
    protected void setSetsInCurrentCase(EditCaseView currentCase, List<SubjectCategorySet> sets) {
        currentCase.setSubjectCategoriesSet(sets);
    }

    @Override
    protected Set<String> getCategoriesIdsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getCaseObject().getSubject().getSubjectCategory();
    }

    @Override
    protected List<SubjectCategory> getDissasociatedCategoriesFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getDisassociatedSubjectCategories();
    }

    @Override
    protected void setCategoriesInCurrentCase(EditCaseView currentCase, List<SubjectCategory> categories) {
        currentCase.setSubjectCategories(categories);
    }

    @Override
    protected List<SubjectCategorySet> getAllTriggerSetsFromCurrentCase(EditCaseView currentCase) {
        return currentCase.getAllTriggerSubjectCategories();
    }

    public SubjectCategorySetService getSubjectCategorySetService() {
        return subjectCategorySetService;
    }

    public void setSubjectCategorySetService(SubjectCategorySetService subjectCategorySetService) {
        this.subjectCategorySetService = subjectCategorySetService;
    }
}
