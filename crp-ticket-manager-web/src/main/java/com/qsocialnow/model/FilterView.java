package com.qsocialnow.model;

import java.util.Date;

public class FilterView {

    private ConnotationView connotation;

    private Date startDateTime;

    private Date endDateTime;

    public ConnotationView getConnotation() {
        return connotation;
    }

    public void setConnotation(ConnotationView connotation) {
        this.connotation = connotation;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

}
