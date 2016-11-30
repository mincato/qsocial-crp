package com.qsocialnow.responsedetector.sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.config.QueueConfigurator;
import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.common.queues.QueueServiceFactory;
import com.qsocialnow.common.queues.QueueType;
import com.qsocialnow.common.services.strategies.CheckHistoryTask;
import com.qsocialnow.common.services.strategies.TwitterMessageQueueConsumer;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.service.TwitterDetectorService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient implements CheckHistoryTask<TwitterMessageEvent> {

    private ConfigurationBuilder configurationBuilder;

    private TwitterDetectorService sourceService;

    private QueueConfigurator queueConfig;

    private static Twitter twitter;

    private static final Logger log = LoggerFactory.getLogger(TwitterClient.class);

    private QueueService queueService;

    private QueueProducer<TwitterMessageEvent> queueProducer;

    private QueueConsumer<TwitterMessageEvent> queueConsumer;

    private TwitterConfigurator twitterConfig;

    public TwitterClient(TwitterDetectorService sourceService, QueueConfigurator queueConfig) {
        this.sourceService = sourceService;
        this.queueConfig = queueConfig;
        initQueue();
    }

    private void initQueue() {
        this.queueService = QueueServiceFactory.getInstance().getQueueServiceInstance(
                StringUtils.join(new String[] { QueueType.MESSAGES.type(), "timeline" }, "_"), queueConfig);

        this.queueProducer = new QueueProducer<TwitterMessageEvent>(queueService.getType());
        this.queueConsumer = new TwitterMessageQueueConsumer(queueService.getType(), this);
        queueProducer.addConsumer(queueConsumer);
        queueService.startProducerConsumer(queueProducer, queueConsumer);
    }

    public void initTwitterClient(TwitterConfigurator twitterConfig) {
        this.twitterConfig = twitterConfig;
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey())
                .setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret())
                .setOAuthAccessToken(twitterConfig.getOAuthAccessToken())
                .setOAuthAccessTokenSecret(twitterConfig.getOAuthAccessTokenSecret());
        configurationBuilder.setUserStreamRepliesAllEnabled(true);

        if (twitter == null) {
            twitter = TwitterFactory.getSingleton();
            log.debug("Initializing twitter client succesfully");
            twitter.setOAuthConsumer(twitterConfig.getOAuthConsumerKey(), twitterConfig.getOAuthConsumerSecret());
            AccessToken accessToken = new AccessToken(twitterConfig.getOAuthAccessToken(),
                    twitterConfig.getOAuthAccessTokenSecret());
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    public void checkMentions(TwitterMessageEvent twitterMessageEvent) {
        this.queueProducer.addItem(twitterMessageEvent);
    }

    public void checkHistory(TwitterMessageEvent messageEvent) {
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
            log.error("There was an error trying retrieve user mentions from twitter", e);
            if (twitterConfig.getRetryErrorCodes().contains(e.getErrorCode())
                    || twitterConfig.getRetryStatusCodes().contains(e.getStatusCode())) {
                queueConsumer.changeInitialDelay(queueConfig.getFailDelay());
                queueProducer.addItem(messageEvent);
            } else {
                if (twitterConfig.getBlockErrorCodes().contains(e.getErrorCode())) {
                    // sourceService.blockSource(FilterConstants.MEDIA_TWITTER);
                }
            }

        } finally {

        }
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
