package com.qsocialnow.model;

import com.qsocialnow.common.model.config.SubjectCategorySet;

public class TriggerSubjectCategorySetView {

    private SubjectCategorySet subjectCategorySet;

    private boolean editingStatus = false;

    public SubjectCategorySet getSubjectCategorySet() {
        return subjectCategorySet;
    }

    public void setSubjectCategorySet(SubjectCategorySet subjectCategorySet) {
        this.subjectCategorySet = subjectCategorySet;
    }

    public boolean isEditingStatus() {
        return editingStatus;
    }

    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
