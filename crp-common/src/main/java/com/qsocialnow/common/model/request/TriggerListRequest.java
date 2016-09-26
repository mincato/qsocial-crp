package com.qsocialnow.common.model.request;

import com.qsocialnow.common.model.config.Status;

public class TriggerListRequest {

    private final String name;

    private final Status status;

    private final String fromDate;

    private final String toDate;

    public TriggerListRequest(String name, Status status, String fromDate, String toDate) {
        this.name = name;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getName() {
        return name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public Status getStatus() {
        return status;
    }

    public String getToDate() {
        return toDate;
    }

}
