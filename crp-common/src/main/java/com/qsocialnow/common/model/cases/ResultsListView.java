package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class ResultsListView implements Serializable {

    private static final long serialVersionUID = -5415986092028442773L;

    private String resolution;

    private Long total;

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
}
