package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class ResultsListView implements Serializable {

    private static final long serialVersionUID = -5415986092028442773L;

    private String idResolution;

    private String resolution;

    private String unitAdmin;

    private String assigned;

    private String status;

    private String pending;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getUnitAdmin() {
        return unitAdmin;
    }

    public void setUnitAdmin(String unitAdmin) {
        this.unitAdmin = unitAdmin;
    }
}
