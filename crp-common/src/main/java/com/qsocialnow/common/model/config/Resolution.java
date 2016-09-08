package com.qsocialnow.common.model.config;

import org.hibernate.validator.constraints.NotBlank;

public class Resolution {

    private String id;

    @NotBlank(message = "{field.empty}")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
