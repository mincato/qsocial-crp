package com.qsocialnow.common.model.config;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class BaseUserResolver implements Serializable {

    private static final long serialVersionUID = 4883096238885894479L;

    private String id;

    private Long source;

    @NotBlank(message = "app.field.empty.validation")
    private String identifier;

    public BaseUserResolver() {
    }

    public BaseUserResolver(BaseUserResolver userResolver) {
        this.setId(userResolver.getId());
        this.setSource(userResolver.getSource());
        this.setIdentifier(userResolver.getIdentifier());
    }

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
