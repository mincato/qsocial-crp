package com.qsocialnow.viewmodel.subjectcategoryset;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.SubjectCategory;

public class SubjectCategoryView {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private boolean active = true;

    public SubjectCategoryView() {
        super();
    }

    public SubjectCategoryView(SubjectCategory category) {
        super();
        this.id = category.getId();
        this.description = category.getDescription();
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
