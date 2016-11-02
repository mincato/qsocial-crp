package com.qsocialnow.responsedetector.sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.service.SourceDetectorService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient {

    private ConfigurationBuilder configurationBuilder;

    private SourceDetectorService sourceService;

    private static Twitter twitter;

    private static final Logger log = LoggerFactory.getLogger(TwitterClient.class);

    public TwitterClient(SourceDetectorService sourceService) {
        this.sourceService = sourceService;
    }

    public void initTwitterClient(TwitterConfigurator twitterConfigurator) {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfigurator.getOAuthConsumerKey())
                .setOAuthConsumerSecret(twitterConfigurator.getOAuthConsumerSecret())
                .setOAuthAccessToken(twitterConfigurator.getOAuthAccessToken())
                .setOAuthAccessTokenSecret(twitterConfigurator.getOAuthAccessTokenSecret());
        configurationBuilder.setUserStreamRepliesAllEnabled(true);

        if (twitter == null) {
            twitter = TwitterFactory.getSingleton();
            log.debug("Initializing twitter client succesfully");
            twitter.setOAuthConsumer(twitterConfigurator.getOAuthConsumerKey(),
                    twitterConfigurator.getOAuthConsumerSecret());
            AccessToken accessToken = new AccessToken(twitterConfigurator.getOAuthAccessToken(),
                    twitterConfigurator.getOAuthAccessTokenSecret());
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    public void checkAnyMention(TwitterMessageEvent messageEvent) {
        try {
            List<Status> statuses = twitter.getUserTimeline();
            log.debug("Starting to read user timeline : " + statuses.size()
                    + " trying to mach responses from message: " + messageEvent.getReplyMessageId());
            for (Status status : statuses) {
                if (String.valueOf(status.getId()).equals(messageEvent.getReplyMessageId())) {
                    log.debug("Finding responses from tweet: " + status.getId() + " retweet count: "
                            + status.getRetweetCount());
                    List<Status> replies = getResponsesFromTweet(status);

                    for (Status statusReply : replies) {
                        User user = statusReply.getUser();
                        if (user != null && messageEvent.getUserId().equals(String.valueOf(user.getId()))) {
                            log.debug("Historical reply detected : " + statusReply.getId() + " Text: "
                                    + statusReply.getText());

                            Date createdDate = statusReply.getCreatedAt();
                            log.debug("Creating event to handle automatic response detection");
                            sourceService.processEvent(true, createdDate.getTime(),
                                    statusReply.getInReplyToScreenName(), null, String.valueOf(statusReply.getId()),
                                    statusReply.getText(), messageEvent.getReplyMessageId(),
                                    String.valueOf(user.getId()), user.getScreenName(), user.getProfileImageURL());
                            break;
                        }
                    }
                }
            }
        } catch (TwitterException e) {
            log.error("Unable to retrieve user mentions :" + e);
        } finally {

        }
        ;
    }

    private List<Status> getResponsesFromTweet(Status status) {

        List<Status> replies = new ArrayList<>();
        long id = status.getId();
        String screenname = status.getUser().getScreenName();
        Query query = new Query("@" + screenname + " since_id:" + id);
        log.debug("Retriving tweets from " + query.getQuery());
        QueryResult result;
        try {
            result = twitter.search(query);
            log.debug("results from tweet: " + result.getTweets().size());

            while (result != null) {
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if (tweet.getInReplyToStatusId() == id) {
                        replies.add(tweet);
                    }
                }
                query = result.nextQuery();
                if (query != null) {
                    result = twitter.search(query);
                } else {
                    result = null;
                }
            }
        } catch (TwitterException e) {
            log.error("Error retrieving tweets :", e);
        }
        return replies;
    }

}
