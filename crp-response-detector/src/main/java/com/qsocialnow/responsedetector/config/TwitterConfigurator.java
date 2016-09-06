package com.qsocialnow.responsedetector.config;

public class TwitterConfigurator {

    private final String OAuthConsumerKey;

    private final String OAuthConsumerSecret;

    private final String OAuthAccessToken;

    private final String OAuthAccessTokenSecret;

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

}
