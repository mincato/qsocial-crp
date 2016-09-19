package com.qsocialnow.common.model.oauth;

public class AuthorizationUrl {

    private final String authorizationUrl;

    public AuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

}
