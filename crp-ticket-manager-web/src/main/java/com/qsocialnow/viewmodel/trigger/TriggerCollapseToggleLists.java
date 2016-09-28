package com.qsocialnow.viewmodel.trigger;

public class TriggerCollapseToggleLists {

    private boolean editingSegments;

    private boolean editingResolutions;

    private boolean editingCaseCategorySets;

    private boolean editingSubjectCategorySets;

    public boolean isEditingSegments() {
        return editingSegments;
    }

    public void setEditingSegments(boolean editingSegments) {
        this.editingSegments = editingSegments;
    }

    public boolean isEditingResolutions() {
        return editingResolutions;
    }

    public void setEditingResolutions(boolean editingResolutions) {
        this.editingResolutions = editingResolutions;
    }

    public boolean isEditingCaseCategorySets() {
        return editingCaseCategorySets;
    }

    public void setEditingCaseCategorySets(boolean editingCaseCategorySets) {
        this.editingCaseCategorySets = editingCaseCategorySets;
    }

    public boolean isEditingSubjectCategorySets() {
        return editingSubjectCategorySets;
    }

    public void setEditingSubjectCategorySets(boolean editingSubjectCategorySets) {
        this.editingSubjectCategorySets = editingSubjectCategorySets;
    }

}
