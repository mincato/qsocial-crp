package com.qsocialnow.common.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.qsocialnow.common.model.cases.ErrorType;

public class FacebookConfig {

    private final String OAuthAppId;

    private final String OAuthAppSecret;

    private final String OAuthAccessToken;

    private final String OAuthPermissions;

    private final String callbackUrl;

    private Set<Integer> retryErrorCodes = new HashSet<>();

    private Set<Integer> retryStatusCodes = new HashSet<>();

    private Map<Integer, ErrorType> errorMapping = new HashMap<Integer, ErrorType>();

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

    public Map<Integer, ErrorType> getErrorMapping() {
        return errorMapping;
    }

    public Set<Integer> getRetryErrorCodes() {
        return retryErrorCodes;
    }

    public Set<Integer> getRetryStatusCodes() {
        return retryStatusCodes;
    }

}
