package com.qsocialnow.services.impl;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.zk.ui.Executions;

import com.qsocialnow.common.config.TwitterConfig;
import com.qsocialnow.common.model.config.SourceCredentials;
import com.qsocialnow.common.model.config.SourceIdentifier;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.config.TwitterConfigFactory;
import com.qsocialnow.services.OauthService;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterOauthService implements OauthService {

    private static final Logger log = LoggerFactory.getLogger(TwitterOauthService.class);

    private final CuratorFramework zookeeperClient;

    private final String twitterConfigZnodePath;

    private TwitterFactory twitterFactory;

    private Twitter twitter;

    private RequestToken requestToken;

    private TwitterConfig twitterConfig;

    public TwitterOauthService(CuratorFramework zookeeperClient, String twitterConfigZnodePath) {
        this.zookeeperClient = zookeeperClient;
        this.twitterConfigZnodePath = twitterConfigZnodePath;

    }

    @Override
    public String getAuthorizationUrl() {
        try {
            if (twitterFactory == null) {
                twitterConfig = TwitterConfigFactory.create(zookeeperClient, twitterConfigZnodePath);
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey());
                configurationBuilder.setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret());
                twitterFactory = new TwitterFactory(configurationBuilder.build());
            }
            twitter = twitterFactory.getInstance();
            requestToken = twitter.getOAuthRequestToken(twitterConfig.getCallbackUrl());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestToken.getAuthorizationURL())
                    .queryParam("force_login", "true").queryParam("screen_name", "diego");
            return builder.toUriString();
        } catch (Exception e) {
            log.error("There was an error trying to get authorization url for twitter", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SourceCredentials getCredentials() {
        try {
            SourceCredentials sourceCredentials = null;
            String verifier = Executions.getCurrent().getParameter("oauth_verifier");
            if (verifier != null) {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                if (accessToken != null) {
                    sourceCredentials = new SourceCredentials();
                    sourceCredentials.setToken(accessToken.getToken());
                    sourceCredentials.setSecretToken(accessToken.getTokenSecret());
                    sourceCredentials.setIdentifier(SourceIdentifier.TWITTER);
                }
            }
            return sourceCredentials;
        } catch (Exception e) {
            log.error("There was an error trying to get credentials for twitter", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolveCredentials(UserResolver userResolver) {
        try {
            SourceCredentials sourceCredentials = null;
            String identifier = null;
            String verifier = Executions.getCurrent().getParameter("oauth_verifier");
            if (verifier != null) {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                if (accessToken != null) {
                    sourceCredentials = new SourceCredentials();
                    sourceCredentials.setToken(accessToken.getToken());
                    sourceCredentials.setSecretToken(accessToken.getTokenSecret());
                    sourceCredentials.setIdentifier(SourceIdentifier.TWITTER);
                    identifier = twitter.verifyCredentials().getScreenName();
                }
            }
            userResolver.setCredentials(sourceCredentials);
            userResolver.setIdentifier(identifier);
        } catch (Exception e) {
            log.error("There was an error trying to get credentials for twitter", e);
            throw new RuntimeException(e);
        }

    }

}
