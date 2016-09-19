package com.qsocialnow.common.model.config;

public class SourceCredentials {

    private String id;

    private SourceIdentifier identifier;

    private String token;

    private String secretToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SourceIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(SourceIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getSecretToken() {
        return secretToken;
    }
}
