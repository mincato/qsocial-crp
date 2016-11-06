package com.qsocialnow.common.model.config;

public class SubjectCategorySetListView {

    private String id;

    private String description;

    private boolean active = true;

    public static SubjectCategorySetListView create(SubjectCategorySet set) {
        SubjectCategorySetListView subjectCategorySetListView = new SubjectCategorySetListView();
        subjectCategorySetListView.setId(set.getId());
        subjectCategorySetListView.setDescription(set.getDescription());
        subjectCategorySetListView.setActive(set.isActive());
        return subjectCategorySetListView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
