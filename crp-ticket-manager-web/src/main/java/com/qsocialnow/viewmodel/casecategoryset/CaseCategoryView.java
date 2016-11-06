package com.qsocialnow.viewmodel.casecategoryset;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.CaseCategory;

public class CaseCategoryView {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private boolean active;

    public CaseCategoryView() {
        super();
    }

    public CaseCategoryView(CaseCategory category) {
        super();
        this.id = category.getId();
        this.description = category.getDescription();
        this.active = category.isActive();
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
