package com.qsocialnow.common.model.config;

public class UserResolver extends BaseUserResolver {

    private static final long serialVersionUID = -4806578437666011569L;

    private SourceCredentials credentials;

    private boolean active;

    private String sourceId;

    public SourceCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(SourceCredentials credentials) {
        this.credentials = credentials;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

}
