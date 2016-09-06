package com.qsocialnow.responsedetector.sources;

import com.qsocialnow.responsedetector.config.TwitterConfigurator;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamClient {

    private TwitterStream twitterStream;

    private FilterQuery tweetFilterQuery;

    private ConfigurationBuilder configurationBuilder;

    public TwitterStreamClient(TwitterConfigurator twitterConfigurator) {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfigurator.getOAuthConsumerKey())
                .setOAuthConsumerSecret(twitterConfigurator.getOAuthConsumerSecret())
                .setOAuthAccessToken(twitterConfigurator.getOAuthAccessToken())
                .setOAuthAccessTokenSecret(twitterConfigurator.getOAuthAccessTokenSecret());
        configurationBuilder.setUserStreamRepliesAllEnabled(true);
    }

    public void initClient() {
        this.twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        this.twitterStream.addListener(new ResponseDetectorStreamListener());
    }

    public void addListeners(StatusListener listener) {
        this.twitterStream.addListener(listener);
    }

    public void removeListeners(StatusListener listener) {
        this.twitterStream.removeListener(listener);
    }

    public void addTrackFilter(String track) {
        tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(new String[] { track }); // OR on keywords
        twitterStream.filter(tweetFilterQuery);
    }

    public void start() {
        twitterStream.user();
    }

    public void stop() {
        twitterStream.cleanUp();
    }
}
