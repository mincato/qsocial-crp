package com.qsocialnow.viewmodel.casecategoryset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.CaseCategorySet;

public class CaseCategorySetView {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private List<CaseCategoryView> categories;

    public CaseCategorySetView() {
        super();
    }

    public CaseCategorySetView(CaseCategorySet caseCategorySet) {
        super();
        this.id = caseCategorySet.getId();
        this.description = caseCategorySet.getDescription();
        if (caseCategorySet.getCategories() != null && caseCategorySet.getCategories().size() > 0) {
            this.setCategories(caseCategorySet.getCategories().stream().map(category -> {
                return new CaseCategoryView(category);
            }).collect(Collectors.toList()));
        } else {
            caseCategorySet.setCategories(new ArrayList<>());
        }
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

    public List<CaseCategoryView> getCategories() {
        return categories;
    }

    public void setCategories(List<CaseCategoryView> categories) {
        this.categories = categories;
    }

}
