package com.qsocialnow.common.model.config;

public class CategoryFilter {

    private Long[] categories;

    private Long categoryGroup;

    public Long[] getCategories() {
        return categories;
    }

    public void setCategories(Long[] categories) {
        this.categories = categories;
    }

    public void setCategoryGroup(Long categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public Long getCategoryGroup() {
        return categoryGroup;
    }

}
