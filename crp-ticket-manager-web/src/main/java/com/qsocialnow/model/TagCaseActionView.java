package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.config.CaseCategory;

public class TagCaseActionView {

    private List<TagCaseCategorySetView> categorySets;

    private List<CaseCategory> categories;

    public List<TagCaseCategorySetView> getCategorySets() {
        return categorySets;
    }

    public void setCategorySets(List<TagCaseCategorySetView> categorySets) {
        this.categorySets = categorySets;
    }

    public List<CaseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CaseCategory> categories) {
        this.categories = categories;
    }
}
