package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class SourceCredentials implements Serializable {

    private static final long serialVersionUID = -8153690238891871012L;

    private SourceIdentifier identifier;

    private String token;

    private String secretToken;

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
