package com.qsocialnow.common.model.config;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class SubjectCategorySet implements Type {

    private String id;

    private String description;

    private List<SubjectCategory> categories;

    private boolean active = true;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public List<SubjectCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategory> categories) {
        this.categories = categories;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SubjectCategorySet set = (SubjectCategorySet) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, set.id).isEquals();
    }
}
