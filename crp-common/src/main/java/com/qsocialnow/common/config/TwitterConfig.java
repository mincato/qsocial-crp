package com.qsocialnow.common.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.qsocialnow.common.model.cases.ErrorType;

public class TwitterConfig {

    private final String OAuthConsumerKey;

    private final String OAuthConsumerSecret;

    private final String callbackUrl;

    private Set<Integer> retryErrorCodes = new HashSet<>();

    private Set<Integer> retryStatusCodes = new HashSet<>();

    private Map<Integer, ErrorType> errorMapping = new HashMap<Integer, ErrorType>();

    public TwitterConfig(final String OAuthConsumerKey, final String OAuthConsumerSecret, final String callbackUrl) {
        this.OAuthConsumerKey = OAuthConsumerKey;
        this.OAuthConsumerSecret = OAuthConsumerSecret;
        this.callbackUrl = callbackUrl;
    }

    public String getOAuthConsumerKey() {
        return OAuthConsumerKey;
    }

    public String getOAuthConsumerSecret() {
        return OAuthConsumerSecret;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public Set<Integer> getRetryErrorCodes() {
        return retryErrorCodes;
    }

    public Set<Integer> getRetryStatusCodes() {
        return retryStatusCodes;
    }

    public Map<Integer, ErrorType> getErrorMapping() {
        return errorMapping;
    }

}
