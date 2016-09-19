package com.qsocialnow.common.config;

public class TwitterConfig {

    private final String OAuthConsumerKey;

    private final String OAuthConsumerSecret;

    public TwitterConfig(final String OAuthConsumerKey, final String OAuthConsumerSecret) {
        this.OAuthConsumerKey = OAuthConsumerKey;
        this.OAuthConsumerSecret = OAuthConsumerSecret;
    }

    public String getOAuthConsumerKey() {
        return OAuthConsumerKey;
    }

    public String getOAuthConsumerSecret() {
        return OAuthConsumerSecret;
    }

}
