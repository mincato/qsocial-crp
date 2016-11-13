package com.qsocialnow.common.model.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class Resolution {

    private String id;

    @NotBlank(message = "app.field.empty.validation")
    private String description;

    private boolean active = true;

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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Resolution resolution = (Resolution) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, resolution.id).isEquals();
    }

}
