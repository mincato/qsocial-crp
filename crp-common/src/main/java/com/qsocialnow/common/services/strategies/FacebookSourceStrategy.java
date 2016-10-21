package com.qsocialnow.common.services.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.config.FacebookConfig;
import com.qsocialnow.common.exception.SourceException;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookSourceStrategy implements SourceStrategy {

    private static final Logger log = LoggerFactory.getLogger(FacebookSourceStrategy.class);

    private FacebookConfig facebookConfig;

    @Override
    public String sendResponse(Case caseObject, UserResolver userResolver, String text) {
        return postMessage(caseObject, userResolver, text);
    }

    @Override
    public String sendMessage(Case caseObject, UserResolver userResolver, String text) {
        return postMessage(caseObject, userResolver, text);
    }

    private String postMessage(Case caseObject, UserResolver userResolver, String text) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthAppId(facebookConfig.getOAuthAppId());
        configurationBuilder.setOAuthAppSecret(facebookConfig.getOAuthAppSecret());
        FacebookFactory facebookFactory = new FacebookFactory(configurationBuilder.build());
        AccessToken accessToken = new AccessToken(userResolver.getCredentials().getToken());
        Facebook facebook = facebookFactory.getInstance(accessToken);
        try {
            if (caseObject.getLastPostId() == null) {
                throw new SourceException("The case does not have a post to reply");
            } else {
                String id;
                if (caseObject.getTriggerEvent() != null
                        && caseObject.getTriggerEvent().getIdOriginal().equals(caseObject.getLastPostId())) {
                    id = caseObject.getLastPostId();
                } else {
                    Reading reading = new Reading();
                    reading.addParameter("fields", "parent");
                    Comment comment = facebook.getComment(caseObject.getLastPostId(), reading);
                    comment = comment.getParent();

                    log.info("Reading comment in facebook strategy:" + comment);

                    if (comment != null) {
                        id = comment.getId();
                    } else {
                        id = caseObject.getLastPostId();
                    }
                }
                return facebook.commentPost(id, text);
            }
        } catch (FacebookException e) {
            log.error("There was an error trying to send response via facebook", e);
            throw new SourceException(e);
        }
    }

    public void setFacebookConfig(FacebookConfig facebookConfig) {
        this.facebookConfig = facebookConfig;
    }
}
