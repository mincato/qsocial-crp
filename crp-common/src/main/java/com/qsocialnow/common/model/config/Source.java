package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class Source implements Serializable {

    private static final long serialVersionUID = -8628077581501373670L;

    private Long id;

    private boolean manual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

}
