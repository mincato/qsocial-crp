package com.qsocialnow.viewmodel.subjectcategoryset;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.SubjectCategorySet;

public class SubjectCategorySetView {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private List<SubjectCategoryView> categories;

    private boolean active = true;

    public SubjectCategorySetView() {
        super();
    }

    public SubjectCategorySetView(SubjectCategorySet subjectCategorySet) {
        super();
        this.id = subjectCategorySet.getId();
        this.description = subjectCategorySet.getDescription();
        this.active = subjectCategorySet.isActive();
        this.setCategories(subjectCategorySet.getCategories().stream().map(category -> {
            return new SubjectCategoryView(category);
        }).collect(Collectors.toList()));

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

    public List<SubjectCategoryView> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategoryView> categories) {
        this.categories = categories;
    }

}
