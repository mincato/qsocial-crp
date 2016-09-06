package com.qsocialnow.responsedetector.sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.service.EventProcessor;

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

    private EventProcessor eventProcessor;

    private static Twitter twitter;

    private static final Logger log = LoggerFactory.getLogger(TwitterClient.class);

    public TwitterClient(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
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
            log.info("Initializing twitter client succesfully");
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
            log.info("Starting to read user timeline : " + statuses.size() + " trying to mach responses from message: "
                    + messageEvent.getReplyMessageId());
            for (Status status : statuses) {
                if (String.valueOf(status.getId()).equals(messageEvent.getReplyMessageId())) {
                    log.info("Finding responses from tweet: " + status.getId() + " retweet count: "
                            + status.getRetweetCount());
                    List<Status> replies = getResponsesFromTweet(status);

                    for (Status statusReply : replies) {
                        User user = statusReply.getUser();
                        if (user != null && messageEvent.getUserId().equals(String.valueOf(user.getId()))) {
                            log.info("Historical reply detected : " + statusReply.getId() + " Text: "
                                    + statusReply.getText());
                            InPutBeanDocument event = new InPutBeanDocument();
                            event.setId(messageEvent.getEventId());
                            event.setTexto(statusReply.getText());
                            event.setFechaCreacion(new Date());
                            event.setResponseDetected(true);

                            log.info("Creating event to handle automatic response detection");
                            eventProcessor.process(event);
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
        log.info("Retriving tweets from " + query.getQuery());
        QueryResult result;
        try {
            result = twitter.search(query);
            log.info("results from tweet: " + result.getTweets().size());

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
