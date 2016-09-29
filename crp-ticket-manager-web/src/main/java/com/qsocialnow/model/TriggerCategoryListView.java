package com.qsocialnow.model;

import java.util.List;
import java.util.Map;

public class TriggerCategoryListView<T1, T2> {

    private List<T1> categorySets;

    private Map<String, T2> categories;

    private boolean enabledAdd = true;

    public List<T1> getCategorySets() {
        return categorySets;
    }

    public void setCategorySets(List<T1> categorySets) {
        this.categorySets = categorySets;
    }

    public Map<String, T2> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, T2> categories) {
        this.categories = categories;
    }

    public boolean isEnabledAdd() {
        return enabledAdd;
    }

    public void setEnabledAdd(boolean enabledAdd) {
        this.enabledAdd = enabledAdd;
    }

}
