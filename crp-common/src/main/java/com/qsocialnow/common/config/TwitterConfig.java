package com.qsocialnow.common.config;

public class TwitterConfig {

    private final String OAuthConsumerKey;

    private final String OAuthConsumerSecret;

    private final String callbackUrl;

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

}
