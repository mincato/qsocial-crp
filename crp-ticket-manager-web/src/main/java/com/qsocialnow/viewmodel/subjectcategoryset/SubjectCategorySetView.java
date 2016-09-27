package com.qsocialnow.viewmodel.subjectcategoryset;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.Subject;

public class SubjectCategorySetView {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String description;

    private List<SubjectCategoryView> categories;

    public SubjectCategorySetView() {
        super();
    }

    public SubjectCategorySetView(Subject subjectCategorySet) {
        super();
        this.id = subjectCategorySet.getId();
        this.description = subjectCategorySet.getDescription();
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

    public List<SubjectCategoryView> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategoryView> categories) {
        this.categories = categories;
    }

}
