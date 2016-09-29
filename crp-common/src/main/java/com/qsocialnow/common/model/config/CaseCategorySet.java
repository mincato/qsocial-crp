package com.qsocialnow.common.model.config;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class CaseCategorySet {

    private String id;

    private String description;

    private List<CaseCategory> categories;

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

    public List<CaseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CaseCategory> categories) {
        this.categories = categories;
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
        CaseCategorySet set = (CaseCategorySet) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, set.id).isEquals();
    }
}
