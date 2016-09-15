package com.qsocialnow.service.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.config.TwitterConfigurator;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSourceStrategy implements SourceStrategy {

    private static final Logger log = LoggerFactory.getLogger(TwitterSourceStrategy.class);

    @Autowired
    private TwitterConfigurator twitterConfigurator;

    @Override
    public void sendResponse(Case caseObject, UserResolver userResolver, String text) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfigurator.getOAuthConsumerKey());
        configurationBuilder.setOAuthConsumerSecret(twitterConfigurator.getOAuthConsumerSecret());
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        AccessToken accessToken = new AccessToken(twitterConfigurator.getOAuthAccessToken(),
                twitterConfigurator.getOAuthAccessTokenSecret());
        Twitter twitter = twitterFactory.getInstance(accessToken);
        text = "@" + caseObject.getSourceUser() + " " + text;
        try {
            twitter.updateStatus(new StatusUpdate(text).inReplyToStatusId(caseObject.getLastPostId()));
        } catch (TwitterException e) {
            log.error("There was an error trying to send response via twitter", e);
            throw new RuntimeException(e);
        }
    }

}
