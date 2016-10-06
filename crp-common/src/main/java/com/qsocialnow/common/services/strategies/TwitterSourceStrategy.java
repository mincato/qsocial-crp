package com.qsocialnow.common.services.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.config.TwitterConfig;
import com.qsocialnow.common.exception.SourceException;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSourceStrategy implements SourceStrategy {

    private static final Logger log = LoggerFactory.getLogger(TwitterSourceStrategy.class);

    private TwitterConfig twitterConfig;

    @Override
    public String sendResponse(Case caseObject, UserResolver userResolver, String text) {
        return sendMessage(caseObject, userResolver, text, caseObject.getLastPostId());
    }

    @Override
    public String sendMessage(Case caseObject, UserResolver userResolver, String text) {
        return sendMessage(caseObject, userResolver, text, null);
    }

    private String sendMessage(Case caseObject, UserResolver userResolver, String text, String statusId) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey());
        configurationBuilder.setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret());
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        AccessToken accessToken = new AccessToken(userResolver.getCredentials().getToken(), userResolver
                .getCredentials().getSecretToken());
        Twitter twitter = twitterFactory.getInstance(accessToken);
        text = "@" + caseObject.getSourceUser() + " " + text;
        try {

            StatusUpdate statusUpdate = new StatusUpdate(text);
            if (statusId != null) {
                statusUpdate.inReplyToStatusId(Long.parseLong(statusId));
            }
            Status status = twitter.updateStatus(statusUpdate);
            return String.valueOf(status.getId());
        } catch (TwitterException e) {
            log.error("There was an error trying to send response via twitter", e);
            throw new SourceException(e);
        }

    }

    public void setTwitterConfig(TwitterConfig twitterConfig) {
        this.twitterConfig = twitterConfig;
    }

}
