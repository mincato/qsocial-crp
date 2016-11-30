package com.qsocialnow.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.responsedetector.FacebookFeedEvent;
import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.common.util.FilterConstants;

@Component
public class ConversationUpdater {

    private static final Logger log = LoggerFactory.getLogger(ConversationUpdater.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.facebook.conversations.path}")
    private String facebookConversationsZnodePath;

    @Value("${app.twitter.users.path}")
    private String twitterUsersZnodePath;

    public void removeConversation(Case caseObject) {
        try {
            if (FilterConstants.MEDIA_FACEBOOK.equals(caseObject.getSource())) {
                String zNodePath = facebookConversationsZnodePath + "/" + caseObject.getId();
                zookeeperClient.delete().forPath(zNodePath);
            }
        } catch (Exception e) {
            log.error("Unexpected error trying to remove conversation node to be consumed by response", e);
        }
    }

    public void createNewConversation(BaseUserResolver userResolver, Case caseObject, String lastPostId, String postId) {
        try {
            if (FilterConstants.MEDIA_TWITTER.equals(userResolver.getSource())) {
                String clientTwitterUsersZnodePath = twitterUsersZnodePath;
                TwitterMessageEvent messageEvent = new TwitterMessageEvent(caseObject.getId(), caseObject.getSubject()
                        .getSourceId(), postId, lastPostId);

                String twitterEvent = new Gson().toJson(messageEvent);
                log.info("Adding a twitter user converstation:" + twitterEvent);
                zookeeperClient.create().forPath(
                        clientTwitterUsersZnodePath + "/" + userResolver.getIdentifier() + "/" + postId,
                        twitterEvent.getBytes());
            }

            if (FilterConstants.MEDIA_FACEBOOK.equals(userResolver.getSource())) {
                String clientFacebookConversationsZnodePath = facebookConversationsZnodePath;
                String sourceId = caseObject.getSubject().getOriginalSourceId() != null ? caseObject.getSubject()
                        .getOriginalSourceId() : caseObject.getSubject().getSourceId();
                FacebookFeedEvent facebookFeedEvent = new FacebookFeedEvent(caseObject.getId(), sourceId, caseObject
                        .getSubject().getIdentifier(), postId, lastPostId, caseObject.getIdRootComment(),
                        userResolver.getIdentifier(), caseObject.getTriggerEvent().getIdOriginal());

                String facebookEvent = new Gson().toJson(facebookFeedEvent);

                log.info("Adding a facebook user converstation:" + facebookEvent);

                String zNodePath = clientFacebookConversationsZnodePath + "/" + caseObject.getId();
                Stat stat = zookeeperClient.checkExists().forPath(zNodePath);
                if (stat == null) {
                    zookeeperClient.create().forPath(zNodePath, facebookEvent.getBytes());
                } else {
                    zookeeperClient.setData().forPath(zNodePath, facebookEvent.getBytes());
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error trying to creade conversation node to be consumed by response", e);
        }
    }

}
