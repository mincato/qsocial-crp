package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;

public class TagSubjectCategorySetView {

    private SubjectCategorySet categorySet;

    private List<SubjectCategory> categories;

    private boolean editingStatus = false;

    public SubjectCategorySet getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(SubjectCategorySet categorySet) {
        this.categorySet = categorySet;
    }

    public List<SubjectCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategory> categories) {
        this.categories = categories;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
