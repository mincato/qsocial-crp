package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.List;

public class CaseAggregationReport implements Serializable {

    private static final long serialVersionUID = 6109082909144326720L;

    private List<ResultsListView> items;

    private String language;

    private String timeZone;

    public List<ResultsListView> getItems() {
        return items;
    }

    public void setItems(List<ResultsListView> items) {
        this.items = items;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
