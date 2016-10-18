package com.qsocialnow.services.impl;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;

import com.qsocialnow.common.config.FacebookConfig;
import com.qsocialnow.common.model.config.SourceCredentials;
import com.qsocialnow.common.model.config.SourceIdentifier;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.config.FacebookConfigFactory;
import com.qsocialnow.services.OauthService;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookOauthService implements OauthService {

    private static final Logger log = LoggerFactory.getLogger(FacebookOauthService.class);

    private final CuratorFramework zookeeperClient;

    private final String facebookConfigZnodePath;

    private FacebookFactory userFacebookFactory;

    private FacebookFactory adminFacebookFactory;

    private Facebook userFacebook;

    private Facebook adminFacebook;

    private FacebookConfig facebookConfig;

    public FacebookOauthService(CuratorFramework zookeeperClient, String facebookConfigZnodePath) {
        this.zookeeperClient = zookeeperClient;
        this.facebookConfigZnodePath = facebookConfigZnodePath;

    }

    @Override
    public String getAuthorizationUrl() {
        try {
            if (userFacebookFactory == null) {
                facebookConfig = FacebookConfigFactory.create(zookeeperClient, facebookConfigZnodePath);
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthAppId(facebookConfig.getOAuthAppId());
                configurationBuilder.setOAuthAppSecret(facebookConfig.getOAuthAppSecret());
                configurationBuilder.setOAuthPermissions(facebookConfig.getOAuthPermissions());
                userFacebookFactory = new FacebookFactory(configurationBuilder.build());

                configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthAppId(facebookConfig.getOAuthAppId());
                configurationBuilder.setOAuthAppSecret(facebookConfig.getOAuthAppSecret());
                configurationBuilder.setOAuthAccessToken(facebookConfig.getOAuthAccessToken());
                adminFacebookFactory = new FacebookFactory(configurationBuilder.build());
                adminFacebook = adminFacebookFactory.getInstance();
            }
            userFacebook = userFacebookFactory.getInstance();
            return userFacebook.getOAuthAuthorizationURL(facebookConfig.getCallbackUrl());
        } catch (Exception e) {
            log.error("There was an error trying to get authorization url for facebook", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolveCredentials(UserResolver userResolver) {
        try {
            SourceCredentials sourceCredentials = null;
            String identifier = null;
            String oauthCode = Executions.getCurrent().getParameter("code");
            if (oauthCode != null) {
                AccessToken accessToken = userFacebook.getOAuthAccessToken(oauthCode);
                if (accessToken != null) {
                    AccessToken extendedAccessToken = userFacebook.extendTokenExpiration(accessToken.getToken());
                    if (extendedAccessToken != null) {
                        ResponseList<Account> accounts = adminFacebook.getAccounts();
                        String pageAccessToken = accounts.get(0).getAccessToken();
                        if (pageAccessToken != null) {
                            sourceCredentials = new SourceCredentials();
                            sourceCredentials.setToken(accessToken.getToken());
                            sourceCredentials.setIdentifier(SourceIdentifier.FACEBOOK);
                            identifier = userFacebook.getMe().getName();
                        }
                    }
                }
            }
            userResolver.setCredentials(sourceCredentials);
            userResolver.setIdentifier(identifier);
        } catch (Exception e) {
            log.error("There was an error trying to get credentials for facebook", e);
            throw new RuntimeException(e);
        }

    }

}
