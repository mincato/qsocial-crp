package com.qsocialnow.factories;

import java.text.MessageFormat;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.services.OauthService;
import com.qsocialnow.services.impl.FacebookOauthService;
import com.qsocialnow.services.impl.TwitterOauthService;

@Component("oauthServiceFactory")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthServiceFactory {

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.twitter.app.configurator.path}")
    private String twitterConfigZnodePath;

    @Value("${app.facebook.app.configurator.path}")
    private String facebookConfigZnodePath;

    public OauthService createService(Long sourceId, String client) {
        if (FilterConstants.MEDIA_TWITTER.equals(sourceId)) {
            String clientTwitterConfigZnodePath = MessageFormat.format(twitterConfigZnodePath, client);
            return new TwitterOauthService(zookeeperClient, clientTwitterConfigZnodePath);
        }
        if (FilterConstants.MEDIA_FACEBOOK.equals(sourceId)) {
            String clientFacebookConfigZnodePath = MessageFormat.format(facebookConfigZnodePath, client);
            return new FacebookOauthService(zookeeperClient, clientFacebookConfigZnodePath);
        }
        return null;
    }

}
