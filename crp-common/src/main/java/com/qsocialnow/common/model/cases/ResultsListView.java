package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class ResultsListView implements Serializable {

    private static final long serialVersionUID = -5415986092028442773L;

    private String idResolution;

    private String resolution;

    private String assigned;

    private Long total;

    public String getIdResolution() {
        return idResolution;
    }

    public void setIdResolution(String idResolution) {
        this.idResolution = idResolution;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }
}
