package com.qsocialnow.elasticsearch.repositories;

import java.util.ArrayList;
import java.util.List;

public class ShouldConditionsFilter {

    private List<ShouldFilter> conditions = new ArrayList<ShouldFilter>();

    public ShouldConditionsFilter() {

    }

    public List<ShouldFilter> getConditions() {
        return conditions;
    }

    public void setConditions(List<ShouldFilter> conditions) {
        this.conditions = conditions;
    }

    public void addShouldCondition(ShouldFilter shouldFilter) {
        this.conditions.add(shouldFilter);
    }
}
