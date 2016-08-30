package com.qsocialnow.responsedetector.sources;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient {

	private TwitterStream twitterStream;
	
	private ConfigurationBuilder configurationBuilder;
	
	public TwitterClient(){
		configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey("3urjVF7PN4bVATDa2hndrhfOI")
                .setOAuthConsumerSecret("mpVGF9xtwY6TXDsBTn9bFLpIKoyXgwbMj9Ym7x7aq4dgdNOLdl")
                .setOAuthAccessToken("768149646548041729-8UUVuboZZxpHctDZFtCDuz9lTdIYjXO")
                .setOAuthAccessTokenSecret("2DqixWkUMYBTiImApgsHofUjUmabZHhiEKzXFiJf8CRTG");
	}
	
	public void initClient(){

		twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
		
	}
	
	
}
