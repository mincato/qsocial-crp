package com.qsocialnow.common.model.retroactive;

public class RetroactiveProcessProgress {

    private RetroactiveProcessStatus status;

    private Long eventsProcessed;

    private String errorMessage;

    public RetroactiveProcessStatus getStatus() {
        return status;
    }

    public void setStatus(RetroactiveProcessStatus status) {
        this.status = status;
    }

    public Long getEventsProcessed() {
        return eventsProcessed;
    }

    public void setEventsProcessed(Long eventsProcessed) {
        this.eventsProcessed = eventsProcessed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
