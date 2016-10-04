package com.qsocialnow.responsedetector.sources;

import java.util.ArrayList;
import java.util.List;

import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.service.SourceDetectorService;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamClient {

    private TwitterStream twitterStream;

    private FilterQuery tweetFilterQuery;

    private ConfigurationBuilder configurationBuilder;

    private List<String> mentionToTrackList;

    public TwitterStreamClient(TwitterConfigurator twitterConfigurator) {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfigurator.getOAuthConsumerKey())
                .setOAuthConsumerSecret(twitterConfigurator.getOAuthConsumerSecret())
                .setOAuthAccessToken(twitterConfigurator.getOAuthAccessToken())
                .setOAuthAccessTokenSecret(twitterConfigurator.getOAuthAccessTokenSecret());

        mentionToTrackList = new ArrayList<>();
        configurationBuilder.setUserStreamRepliesAllEnabled(true);
    }

    public void initClient(SourceDetectorService sourceDetectorService) {
        this.twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        this.twitterStream.addListener(new ResponseDetectorStreamListener(sourceDetectorService));
    }

    public void addTrackFilter(String track) {
        mentionToTrackList.add(track);
        tweetFilterQuery = new FilterQuery();

        String[] mentions = new String[mentionToTrackList.size()];
        mentions = mentionToTrackList.toArray(mentions);

        tweetFilterQuery.track(mentions); // OR on keywords
        // init stream to filter user mentions/replies
        twitterStream.filter(tweetFilterQuery);
    }

    public void stop() {
        twitterStream.cleanUp();
    }
}
