package com.qsocialnow.responsedetector.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.qsocialnow.common.model.cases.ErrorType;

public class TwitterConfigurator {

    private final String OAuthConsumerKey;

    private final String OAuthConsumerSecret;

    private final String OAuthAccessToken;

    private final String OAuthAccessTokenSecret;

    private Set<Integer> retryErrorCodes = new HashSet<>();

    private Set<Integer> retryStatusCodes = new HashSet<>();

    private Set<Integer> blockErrorCodes = new HashSet<>();

    private Map<Integer, ErrorType> errorMapping = new HashMap<Integer, ErrorType>();

    public TwitterConfigurator(final String OAuthConsumerKey, final String OAuthConsumerSecret,
            final String OAuthAccessToken, final String OAuthAccessTokenSecret) {

        this.OAuthConsumerKey = OAuthConsumerKey;
        this.OAuthConsumerSecret = OAuthConsumerSecret;
        this.OAuthAccessToken = OAuthAccessToken;
        this.OAuthAccessTokenSecret = OAuthAccessTokenSecret;
    }

    public String getOAuthConsumerKey() {
        return OAuthConsumerKey;
    }

    public String getOAuthConsumerSecret() {
        return OAuthConsumerSecret;
    }

    public String getOAuthAccessToken() {
        return OAuthAccessToken;
    }

    public String getOAuthAccessTokenSecret() {
        return OAuthAccessTokenSecret;
    }

    public Set<Integer> getRetryErrorCodes() {
        return retryErrorCodes;
    }

    public void setRetryErrorCodes(Set<Integer> retryErrorCodes) {
        this.retryErrorCodes = retryErrorCodes;
    }

    public Set<Integer> getRetryStatusCodes() {
        return retryStatusCodes;
    }

    public void setRetryStatusCodes(Set<Integer> retryStatusCodes) {
        this.retryStatusCodes = retryStatusCodes;
    }

    public Set<Integer> getBlockErrorCodes() {
        return blockErrorCodes;
    }

    public void setBlockErrorCodes(Set<Integer> blockErrorCodes) {
        this.blockErrorCodes = blockErrorCodes;
    }

    public Map<Integer, ErrorType> getErrorMapping() {
        return errorMapping;
    }

    public void setErrorMapping(Map<Integer, ErrorType> errorMapping) {
        this.errorMapping = errorMapping;
    }

}
