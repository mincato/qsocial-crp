package com.qsocialnow.common.model.config;

import org.hibernate.validator.constraints.NotBlank;

public class UserResolver {

    private String id;

    private Long source;

    @NotBlank(message = "{field.empty}")
    private String identifier;

    private SourceCredentials credentials;

    private Boolean active;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SourceCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(SourceCredentials credentials) {
        this.credentials = credentials;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
