package com.qsocialnow.common.model.cases;

public class CasesFilterRequestReport {

    private CasesFilterRequest filterRequest;

    private String language;

    private String timeZone;

    public CasesFilterRequest getFilterRequest() {
        return filterRequest;
    }

    public void setFilterRequest(CasesFilterRequest filterRequest) {
        this.filterRequest = filterRequest;
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
