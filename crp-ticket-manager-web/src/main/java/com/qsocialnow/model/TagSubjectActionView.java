package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.config.SubjectCategory;

public class TagSubjectActionView {

    private List<TagSubjectCategorySetView> categorySets;

    private List<SubjectCategory> categories;

    public List<TagSubjectCategorySetView> getCategorySets() {
        return categorySets;
    }

    public void setCategorySets(List<TagSubjectCategorySetView> categorySets) {
        this.categorySets = categorySets;
    }

    public List<SubjectCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategory> categories) {
        this.categories = categories;
    }

}
