package com.qsocialnow.responsedetector.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.factories.TwitterConfiguratorFactory;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.sources.TwitterClient;
import com.qsocialnow.responsedetector.sources.TwitterStreamClient;

public class TwitterDetectorService extends SourceDetectorService {

    private static final Logger log = LoggerFactory.getLogger(TwitterDetectorService.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private ResponseDetectorConfig appConfig;

    private PathChildrenCache pathChildrenCache;

    @Autowired
    private TwitterConfiguratorFactory twitterConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private TwitterStreamClient twitterStreamClient;

    private TwitterConfigurator configurator;

    private boolean startListening = false;

    private HashMap<String, List<TwitterMessageEvent>> conversations;

    @Override
    public void run() {

        try {

            configurator = twitterConfiguratorFactory.getConfigurator(appConfig.getTwitterAppConfiguratorZnodePath());
            twitterStreamClient = new TwitterStreamClient(configurator);
            twitterStreamClient.initClient(this);
            conversations = new HashMap<String, List<TwitterMessageEvent>>();
            pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getTwitterUsersZnodePath(), true);
            addListener();
            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        String userResolverToFilter = ZKPaths.getNodeFromPath(event.getData().getPath());
                        addUserResolverTrack(userResolverToFilter);
                        if (event.getData() != null) {
                            String userResolverNode = ZKPaths.getNodeFromPath(event.getData().getPath());
                            byte[] messageBytes = event.getData().getData();
                            if (messageBytes != null) {
                                TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                        new String(messageBytes), TwitterMessageEvent.class);
                                if (twitterMessageEvent != null) {
                                    addTwitterMessage(userResolverNode, twitterMessageEvent);
                                }
                            }
                        }
                        break;
                    }
                    case INITIALIZED: {
                        log.info("PathChildrenCache starting to init");
                        List<ChildData> initialData = event.getInitialData();
                        for (ChildData childData : initialData) {

                            String userResolverToFilter = ZKPaths.getNodeFromPath(childData.getPath());
                            log.info("User Resolver - Message added at Init process: " + userResolverToFilter);
                            conversations.put(userResolverToFilter, null);
                            twitterStreamClient.addTrackFilter(userResolverToFilter);

                            if (childData.getData() != null) {
                                byte[] messageBytes = childData.getData();
                                TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                        new String(messageBytes), TwitterMessageEvent.class);
                                if (twitterMessageEvent != null) {
                                    checkMessageResponses(userResolverToFilter, twitterMessageEvent);
                                    log.info("initial node-message: " + twitterMessageEvent.getMessageId());
                                }
                            }
                        }
                        startListening = true;
                        break;
                    }
                    case CHILD_UPDATED: {
                        String userResolverNode = ZKPaths.getNodeFromPath(event.getData().getPath());

                        log.info("Node-Message updated: " + userResolverNode);
                        byte[] messageBytes = event.getData().getData();
                        TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                new String(messageBytes), TwitterMessageEvent.class);

                        addTwitterMessage(userResolverNode, twitterMessageEvent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

    private void addUserResolverTrack(String userResolverToFilter) {
        try {
            if (startListening) {
                log.info("Adding UserResolver:" + userResolverToFilter);
                conversations.put(userResolverToFilter, null);
                twitterStreamClient.addTrackFilter(userResolverToFilter);
            }
        } catch (Exception e) {
            log.error("There was an error adding new User Resolver to track :" + userResolverToFilter, e);
        }
    }

    private void addTwitterMessage(String userResolver, TwitterMessageEvent twitterMessageEvent) {
        try {
            if (startListening) {
                log.info("Adding message conversation from :" + userResolver + " from Case:"
                        + twitterMessageEvent.getCaseId());
                List<TwitterMessageEvent> conversationsByUserResolver = conversations.get(userResolver);
                if (conversationsByUserResolver == null) {
                    conversationsByUserResolver = new ArrayList<TwitterMessageEvent>();
                    log.info("Init conversation list from user :" + userResolver);
                }
                conversationsByUserResolver.add(twitterMessageEvent);
                conversations.put(userResolver, conversationsByUserResolver);
            }
        } catch (Exception e) {
            log.error(
                    "There was an error creating the message handler processor for tweet: "
                            + twitterMessageEvent.getMessageId(), e);
        }
    }

    private void checkMessageResponses(String userResolver, TwitterMessageEvent message) {
        TwitterClient twitterClient = new TwitterClient(this);
        twitterClient.initTwitterClient(configurator);
        twitterClient.checkAnyMention(userResolver, message);
    }

    public void stop() {
        if (twitterStreamClient != null) {
            this.twitterStreamClient.stop();
        }
        try {
            if (pathChildrenCache != null) {
                pathChildrenCache.close();
            }
        } catch (IOException e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    @Override
    public void removeSourceConversation(String userResolver, String converstation) {
        try {
            // zookeeperClient.delete().forPath(appConfig.getTwitterMessagesPath()
            // + "/" + converstation);

        } catch (Exception e) {
            log.error("Unable to remove message conversation:: " + converstation, e);
        }
    }

    @Override
    public void processEvent(Boolean isResponseFromMessage, String userResolver, String[] userMentions,
            String sourceMessageId, String messageText, String inReplyToMessageId, String userId, String userName,
            String userProfileImage) {

        try {
            InPutBeanDocument event = new InPutBeanDocument();

            String mainUserResolver = null;

            event.setId(sourceMessageId);
            event.setFecha(new Date());
            // event.setTipoDeMedio("morbi");
            event.setName(messageText);
            event.setTitulo(messageText);
            event.setTexto(messageText);
            event.setNormalizeMessage(messageText);

            if (userMentions != null) {
                for (int i = 0; i < userMentions.length; i++) {
                    String userMention = userMentions[i];
                    if (conversations.containsKey(userMention)) {
                        mainUserResolver = userMention;
                        break;
                    }
                }
            }

            if (isResponseFromMessage) {
                List<TwitterMessageEvent> conversationsByUserResolver = conversations.get(userResolver);
                for (TwitterMessageEvent twitterMessageEvent : conversationsByUserResolver) {
                    if (twitterMessageEvent.getReplyMessageId().equals(inReplyToMessageId)) {
                        // is response from existing conversation
                        event.setIdPadre(twitterMessageEvent.getEventId());
                        event.setOriginIdCase(twitterMessageEvent.getCaseId());
                        break;
                    }
                }
            } else {
                List<TwitterMessageEvent> conversationsByUserResolver = conversations.get(mainUserResolver);
                for (TwitterMessageEvent twitterMessageEvent : conversationsByUserResolver) {
                    if (twitterMessageEvent.getUserId().equals(userId)) {
                        // is response from existing user
                        event.setIdPadre(twitterMessageEvent.getEventId());
                        event.setOriginIdCase(twitterMessageEvent.getCaseId());
                        break;
                    }
                }
            }
            event.setUsuarioOriginal(userName);
            event.setIdUsuarioOriginal(userId);
            event.setProfileImage(userProfileImage);

            event.setResponseDetected(true);
            event.setFechaCreacion(new Date());
            eventProcessor.process(event);
            log.info("Creating event to handle automatic response detection");
        } catch (Exception e) {
            log.error("Error trying to register event :" + e);
        }
    }
}
