package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.User;

public class EditCaseView {

    private Case caseObject;

    private Segment segment;

    private Trigger trigger;

    private List<BaseUserResolver> userResolverOptions;

    private List<User> userOptions;

    private List<CaseCategory> caseCategories;

    private List<CaseCategorySet> caseCategoriesSet;

    private List<SubjectCategory> subjectCategories;

    private List<SubjectCategorySet> subjectCategoriesSet;

    private List<CaseCategorySet> triggerCategories;

    private List<SubjectCategorySet> triggerSubjectCategories;

    private List<Subject> subjectOptions;

    private List<CaseCategorySet> allTriggerCategories;

    private List<CaseCategory> disassociatedCategories;

    private List<SubjectCategorySet> allTriggerSubjectCategories;

    private List<SubjectCategory> disassociatedSubjectCategories;

    private Media source;

    private String openCaseDeepLinkUrl = null;

    private String teamDescription = null;

    private String resolutionDescription = null;

    public void setCaseObject(Case caseObject) {
        this.caseObject = caseObject;
    }

    public Case getCaseObject() {
        return caseObject;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public List<BaseUserResolver> getUserResolverOptions() {
        return userResolverOptions;
    }

    public void setUserResolverOptions(List<BaseUserResolver> userResolverOptions) {
        this.userResolverOptions = userResolverOptions;
    }

    public List<CaseCategory> getCaseCategories() {
        return caseCategories;
    }

    public void setCaseCategories(List<CaseCategory> caseCategories) {
        this.caseCategories = caseCategories;
    }

    public List<CaseCategorySet> getTriggerCategories() {
        return triggerCategories;
    }

    public void setTriggerCategories(List<CaseCategorySet> triggerCategories) {
        this.triggerCategories = triggerCategories;
    }

    public List<CaseCategorySet> getCaseCategoriesSet() {
        return caseCategoriesSet;
    }

    public void setCaseCategoriesSet(List<CaseCategorySet> caseCategoriesSet) {
        this.caseCategoriesSet = caseCategoriesSet;
    }

    public List<User> getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(List<User> userOptions) {
        this.userOptions = userOptions;
    }

    public List<Subject> getSubjectOptions() {
        return subjectOptions;
    }

    public void setSubjectOptions(List<Subject> subjectOptions) {
        this.subjectOptions = subjectOptions;
    }

    public Media getSource() {
        return source;
    }

    public void setSource(Media source) {
        this.source = source;
    }

    public List<SubjectCategorySet> getTriggerSubjectCategories() {
        return triggerSubjectCategories;
    }

    public void setTriggerSubjectCategories(List<SubjectCategorySet> triggerSubjectCategories) {
        this.triggerSubjectCategories = triggerSubjectCategories;
    }

    public List<SubjectCategory> getSubjectCategories() {
        return subjectCategories;
    }

    public void setSubjectCategories(List<SubjectCategory> subjectCategories) {
        this.subjectCategories = subjectCategories;
    }

    public List<SubjectCategorySet> getSubjectCategoriesSet() {
        return subjectCategoriesSet;
    }

    public void setSubjectCategoriesSet(List<SubjectCategorySet> subjectCategoriesSet) {
        this.subjectCategoriesSet = subjectCategoriesSet;
    }

    public List<CaseCategorySet> getAllTriggerCategories() {
        return allTriggerCategories;
    }

    public void setAllTriggerCategories(List<CaseCategorySet> allTriggerCategories) {
        this.allTriggerCategories = allTriggerCategories;
    }

    public String getOpenCaseDeepLinkUrl() {
        return openCaseDeepLinkUrl;
    }

    public void setOpenCaseDeepLinkUrl(String openCaseDeepLinkUrl) {
        this.openCaseDeepLinkUrl = openCaseDeepLinkUrl;
    }

    public List<CaseCategory> getDisassociatedCategories() {
        return disassociatedCategories;
    }

    public void setDisassociatedCategories(List<CaseCategory> disassociatedCategories) {
        this.disassociatedCategories = disassociatedCategories;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getResolutionDescription() {
        return resolutionDescription;
    }

    public void setResolutionDescription(String resolutionDescription) {
        this.resolutionDescription = resolutionDescription;
    }

    public List<SubjectCategorySet> getAllTriggerSubjectCategories() {
        return allTriggerSubjectCategories;
    }

    public void setAllTriggerSubjectCategories(List<SubjectCategorySet> allTriggerSubjectCategories) {
        this.allTriggerSubjectCategories = allTriggerSubjectCategories;
    }

    public List<SubjectCategory> getDisassociatedSubjectCategories() {
        return disassociatedSubjectCategories;
    }

    public void setDisassociatedSubjectCategories(List<SubjectCategory> disassociatedSubjectCategories) {
        this.disassociatedSubjectCategories = disassociatedSubjectCategories;
    }
}
