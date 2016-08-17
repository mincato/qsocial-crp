package com.qsocialnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BackEndEntity extends BackEndObject {

    private static final long serialVersionUID = -5354058460706112830L;

    @JsonProperty("_id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
