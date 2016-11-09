package com.qsocialnow.responsedetector.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.common.model.responsedetector.FacebookFeedEvent;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.responsedetector.config.FacebookConfigurator;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.factories.FacebookConfiguratorFactory;
import com.qsocialnow.responsedetector.sources.FacebookClient;
import com.qsocialnow.responsedetector.sources.FacebookFeedConsumer;

public class FacebookDetectorService extends SourceDetectorService {

    private static final Logger log = LoggerFactory.getLogger(FacebookDetectorService.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private ResponseDetectorConfig appConfig;

    private TreeCache treeCache;

    @Autowired
    private FacebookConfiguratorFactory facebookConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private FacebookClient facebookClient;

    private FacebookConfigurator configurator;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private FacebookFeedConsumer facebookFeedConsumer;

    private HashMap<String, FacebookFeedEvent> conversations;

    private HashMap<String, String> nodePaths;

    @Override
    public void run() {

        try {

            configurator = facebookConfiguratorFactory.getConfigurator(appConfig.getFacebookAppConfiguratorZnodePath());

            facebookClient = new FacebookClient(configurator, this);
            facebookClient.initClient();
            facebookFeedConsumer = new FacebookFeedConsumer(facebookClient);

            conversations = new HashMap<String, FacebookFeedEvent>();
            nodePaths = new HashMap<String, String>();
            treeCache = new TreeCache(zookeeperClient, appConfig.getFacebookUsersZnodePath());

            addListener();
            treeCache.start(); // start(StartMode.POST_INITIALIZED_EVENT);
            startFeedConsumer();

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
        TreeCacheListener listener = new TreeCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case NODE_ADDED: {
                        String nodeAdded = ZKPaths.getNodeFromPath(event.getData().getPath());
                        String nodePath = event.getData().getPath();

                        log.debug("Adding node:" + nodeAdded + " from path: " + event.getData().getPath());
                        if (event.getData().getData() != null) {
                            String nodeValue = new String(event.getData().getData());
                            log.debug("Adding node value:-" + nodeValue + "-");
                            if (nodeValue.equals("NEW")) {
                                addUserResolverTrack(nodeAdded);
                            } else {
                                // new conversation
                                if (!Strings.isNullOrEmpty(nodeValue)) {
                                    byte[] messageBytes = event.getData().getData();
                                    FacebookFeedEvent facebookFeedEvent = new GsonBuilder().create().fromJson(
                                            new String(messageBytes), FacebookFeedEvent.class);
                                    if (facebookFeedEvent != null) {
                                        addFacebookFeedEvent(nodeAdded, nodePath, facebookFeedEvent);
                                    }
                                } else {
                                    log.debug("Not Adding node with empty value ");
                                }
                            }
                        } else {
                            log.debug("Not Adding node value");
                        }
                        break;
                    }
                    case NODE_UPDATED: {
                        log.debug("Facebook conversation TreeNode changed: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_REMOVED: {
                        log.debug("Facebook conversation removed: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case INITIALIZED: {
                        break;
                    }
                    default:
                        log.debug("Other event: " + event.getType().name());
                }
            }

        };
        treeCache.getListenable().addListener(listener);
    }

    private void addUserResolverTrack(String userResolverToFilter) {
        try {
            log.debug("Adding UserResolver:" + userResolverToFilter);
        } catch (Exception e) {
            log.error("There was an error adding new User Resolver to track :" + userResolverToFilter, e);
        }
    }

    private void addFacebookFeedEvent(String commentId, String commentPath, FacebookFeedEvent facebookFeedEvent) {
        try {

            log.debug("Adding message conversation from comment :" + facebookFeedEvent.getRootCommentId()
                    + " from Case:" + facebookFeedEvent.getCaseId());

            nodePaths.put(facebookFeedEvent.getRootCommentId(), commentPath);
            conversations.put(facebookFeedEvent.getRootCommentId(), facebookFeedEvent);
            facebookClient.addNewConversation(facebookFeedEvent.getRootCommentId());
        } catch (Exception e) {
            log.error("There was an error adding converstaions from userResolver tracking response from id: "
                    + commentId, e);
        }
    }

    public void stop() {
        if (facebookClient != null) {
            this.facebookClient.stop();
        }
        try {
            if (treeCache != null) {
                treeCache.close();
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    @Override
    public void removeSourceConversation(String conversation) {
        try {
            String nodePath = nodePaths.get(conversation);
            log.debug("Removing node after detect response: " + nodePath);
            conversations.remove(conversation);
            zookeeperClient.delete().forPath(nodePath);
        } catch (Exception e) {
            log.error("Unable to remove message conversation: " + conversation, e);
        }
    }

    private void startFeedConsumer() {
        log.debug("Starting Feed Consumer to check new comments from existing posts");
        executor.scheduleWithFixedDelay(facebookFeedConsumer, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void processEvent(Boolean isResponseFromMessage, Long timestamp, String userResolver, String[] userMentions,
            String messageId, String messageText, String commentId, String userId, String userName,
            String userProfileImage) {

        try {
            Event event = new Event();
            event.setId(messageId);

            event.setTimestamp(timestamp);
            event.setFecha(new Date());
            event.setMedioId(FilterConstants.MEDIA_FACEBOOK);
            event.setTitulo(messageText);
            event.setTexto(messageText);
            event.setNormalizeMessage(messageText);

            if (isResponseFromMessage) {
                log.debug("Adding facebookevent information into event");
                FacebookFeedEvent conversationsByCommentId = conversations.get(commentId);
                if (conversationsByCommentId != null) {
                    event.setOriginIdCase(conversationsByCommentId.getCaseId());
                    event.setIdOriginal(conversationsByCommentId.getOriginalId());
                    event.setRootCommentId(conversationsByCommentId.getRootCommentId());
                }
                removeSourceConversation(commentId);
            }
            event.setUsuarioOriginal(userName);
            event.setIdUsuarioOriginal(userId);
            event.setProfileImage(userProfileImage);
            event.setResponseDetected(true);
            event.setFechaCreacion(new Date());
            eventProcessor.process(event);
            log.debug("Creating event to handle automatic response detection");
        } catch (Exception e) {
            log.error("Error trying to register event :" + e);
        }
    }

    @Override
    public String getReplyIdToTrack(String idRootComment) {
        FacebookFeedEvent conversationsByCommentId = conversations.get(idRootComment);
        return conversationsByCommentId != null ? conversationsByCommentId.getCommentId() : null;

    }

    @Override
    public String getUserIdToTrack(String idRootComment) {
        FacebookFeedEvent conversationsByCommentId = conversations.get(idRootComment);
        return conversationsByCommentId != null ? conversationsByCommentId.getUserId() : null;
    }
}
