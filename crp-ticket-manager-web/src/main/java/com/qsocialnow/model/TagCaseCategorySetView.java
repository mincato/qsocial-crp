package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;

public class TagCaseCategorySetView {

    private CaseCategorySet categorySet;

    private List<CaseCategory> categories;

    private boolean editingStatus = false;

    public CaseCategorySet getCategorySet() {
        return categorySet;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

    public void setCategorySet(CaseCategorySet categorySet) {
        this.categorySet = categorySet;
    }

    public List<CaseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CaseCategory> categories) {
        this.categories = categories;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

}
