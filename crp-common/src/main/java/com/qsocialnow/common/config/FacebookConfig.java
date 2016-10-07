package com.qsocialnow.common.config;

public class FacebookConfig {

    private final String OAuthAppId;

    private final String OAuthAppSecret;

    private final String OAuthAccessToken;

    private final String OAuthPermissions;

    private final String callbackUrl;

    public FacebookConfig(final String OAuthAppId, final String OAuthAppSecret, final String OAuthAccessToken,
            final String OAuthPermissions, final String callbackUrl) {
        this.OAuthAppId = OAuthAppId;
        this.OAuthAppSecret = OAuthAppSecret;
        this.OAuthAccessToken = OAuthAccessToken;
        this.OAuthPermissions = OAuthPermissions;
        this.callbackUrl = callbackUrl;
    }

    public String getOAuthAppId() {
        return OAuthAppId;
    }

    public String getOAuthAppSecret() {
        return OAuthAppSecret;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getOAuthAccessToken() {
        return OAuthAccessToken;
    }

    public String getOAuthPermissions() {
        return OAuthPermissions;
    }

}
