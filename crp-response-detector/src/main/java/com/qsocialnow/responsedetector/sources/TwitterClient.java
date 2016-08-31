package com.qsocialnow.responsedetector.sources;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient {

	private TwitterStream twitterStream;
	
	private FilterQuery tweetFilterQuery;
	
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
		initialConfiguration();
	}
	
	public void addListeners(StatusListener listener){
		this.twitterStream.addListener(listener);
	}
	
	private void initialConfiguration(){
		
		tweetFilterQuery = new FilterQuery(); // See 
		tweetFilterQuery.track(new String[]{"Batman","#Batman"}); // OR on keywords
		//tweetFilterQuery.locations(new double[][]{new double[]{-126.562500,30.448674},new double[]{-61.171875,44.087585}}); // See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc. 
		//Note that not all tweets have location metadata set.
		//tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets
		

	}
	
	public void startFiltering(){
		twitterStream.filter(tweetFilterQuery);
	}
	
	
}
