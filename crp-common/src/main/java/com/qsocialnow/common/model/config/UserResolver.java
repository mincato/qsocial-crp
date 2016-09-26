package com.qsocialnow.common.model.config;

public class UserResolver extends BaseUserResolver {

    private SourceCredentials credentials;

    private Boolean active;

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
