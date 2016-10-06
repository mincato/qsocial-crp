package com.qsocialnow.responsedetector.config;

public class FacebookConfigurator {

    private final String OAuthAppId;

    private final String OAuthAppSecret;

    private final String OAuthAccessToken;


    public FacebookConfigurator(final String OAuthAppId, final String OAuthAppSecret,
            final String OAuthAccessToken) {

        this.OAuthAppId = OAuthAppId;
        this.OAuthAppSecret = OAuthAppSecret;
        this.OAuthAccessToken = OAuthAccessToken;

    }
	public String getOAuthAppId() {
		return OAuthAppId;
	}

	public String getOAuthAppSecret() {
		return OAuthAppSecret;
	}

	public String getOAuthAccessToken() {
		return OAuthAccessToken;
	}
}
