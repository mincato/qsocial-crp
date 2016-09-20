package com.qsocialnow.common.model.config;

import org.hibernate.validator.constraints.NotBlank;

public class BaseUserResolver {

    private String id;

    private Long source;

    @NotBlank(message = "{field.empty}")
    private String identifier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

}
