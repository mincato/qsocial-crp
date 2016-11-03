package com.qsocialnow.service.action;

import java.text.MessageFormat;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.responsedetector.FacebookFeedEvent;
import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.common.services.strategies.SourceStrategy;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.persistence.UserResolverRepository;

@Component("sendResponseCaseAction")
public class SendResponseCaseAction implements Action {

    private Logger log = LoggerFactory.getLogger(SendResponseCaseAction.class);

    @Resource
    private Map<Long, SourceStrategy> sources;

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.twitter.users.path}")
    private String twitterUsersZnodePath;

    @Value("${app.client}")
    private String appClient;

    @Value("${app.facebook.users.path}")
    private String facebookUsersZnodePath;

    @Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        String userResolverId;
        if (caseObject.getUserResolver() == null) {
            userResolverId = (String) parameters.get(ActionParameter.USER_RESOLVER);
        } else {
            userResolverId = caseObject.getUserResolver().getId();
        }
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        String lastPostId = caseObject.getLastPostId();
        String postId = sources.get(userResolver.getSource()).sendResponse(caseObject, userResolver, text);

        if (caseObject.getIdRootComment() == null)
            caseObject.setIdRootComment(postId);

        caseObject.setPendingResponse(false);
        caseObject.setLastPostId(postId);
        caseObject.setUserResolver(new BaseUserResolver(userResolver));
        caseObject.addMessage(new Message(postId, false));
        parameters.put(ActionParameter.COMMENT, text);

        createNewUserResolverUserNode(userResolver, caseObject, lastPostId, postId);
    }

    private void createNewUserResolverUserNode(UserResolver userResolver, Case caseObject, String lastPostId,
            String postId) {
        try {
            if (FilterConstants.MEDIA_TWITTER.equals(userResolver.getSource())) {
                String clientTwitterUsersZnodePath = MessageFormat.format(twitterUsersZnodePath, appClient);
                TwitterMessageEvent messageEvent = new TwitterMessageEvent(caseObject.getId(), lastPostId, caseObject
                        .getSubject().getSourceId(), postId, lastPostId);

                String twitterEvent = new Gson().toJson(messageEvent);
                log.info("Adding a twitter user converstation:" + twitterEvent);
                zookeeperClient.create().forPath(
                        clientTwitterUsersZnodePath + "/" + userResolver.getIdentifier() + "/" + postId,
                        twitterEvent.getBytes());
            }

            if (FilterConstants.MEDIA_FACEBOOK.equals(userResolver.getSource())) {
                String clientFacebookUsersZnodePath = MessageFormat.format(facebookUsersZnodePath, appClient);
                FacebookFeedEvent facebookFeedEvent = new FacebookFeedEvent(caseObject.getId(),
                        caseObject.getLastPostId(), caseObject.getSubject().getSourceId(),
                        caseObject.getSubject().getIdentifier(), 
                        postId, lastPostId, 
                        caseObject.getIdRootComment(),
                        userResolver.getIdentifier(), caseObject.getTriggerEvent().getIdOriginal());

                String facebookEvent = new Gson().toJson(facebookFeedEvent);

                log.info("Adding a facebook user converstation:" + facebookEvent);
                zookeeperClient.create().forPath(
                        clientFacebookUsersZnodePath + "/" + userResolver.getIdentifier() + "/"
                                + caseObject.getIdRootComment(), facebookEvent.getBytes());
            }
        } catch (Exception e) {
            log.error("Unexpected error trying to creade conversation node to be consumed by response", e);
        }
    }

}
