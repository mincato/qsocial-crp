package com.qsocialnow.responsedetector.sources;

import java.util.List;

import com.qsocialnow.responsedetector.config.FacebookConfigurator;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;


public class FacebookClient {

    private ConfigurationBuilder configurationBuilder;
    
    private Facebook facebook;

	public FacebookClient(FacebookConfigurator facebookConfigurator) {

		configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthAppId(facebookConfigurator.getOAuthAppId())
				.setOAuthAppSecret(facebookConfigurator.getOAuthAppSecret())
				.setOAuthAccessToken(facebookConfigurator.getOAuthAccessToken());
	}

    public void initClient() {
        facebook = new FacebookFactory(configurationBuilder.build()).getInstance();
    }

    public void addTrackFilter(String track) {

    }

    public void addTrackFilters(List<String> tracks) {
    }

    public void stop() {
        
    }
}
