package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModelList;

public class CategoryFilterView {

    private CategoryGroup categoryGroup;

    private List<Category> categories;

    private List<Category> categoryOptions = new ArrayList<>();

    private final List<CategoryGroup> categoryGroups;

    public CategoryFilterView(List<CategoryGroup> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategoryOptions() {
        if (categoryGroup != null && categoryOptions.isEmpty()) {
            categoryOptions.addAll(categoryGroups.stream()
                    .filter(categoryGroup -> this.categoryGroup.getId().equals(categoryGroup.getId())).findFirst()
                    .get().getCategorias());
        }
        return categoryOptions;
    }

    public void setCategoryOptions(ListModelList<Category> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }
}
