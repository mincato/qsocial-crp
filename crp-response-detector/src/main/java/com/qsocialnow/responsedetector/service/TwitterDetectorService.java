package com.qsocialnow.responsedetector.service;

import java.util.Date;
import java.util.HashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
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
import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.factories.TwitterConfiguratorFactory;
import com.qsocialnow.responsedetector.sources.TwitterClient;
import com.qsocialnow.responsedetector.sources.TwitterStreamClient;

public class TwitterDetectorService extends SourceDetectorService {

    private static final Logger log = LoggerFactory.getLogger(TwitterDetectorService.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private ResponseDetectorConfig appConfig;

    private TreeCache treeCache;

    @Autowired
    private TwitterConfiguratorFactory twitterConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private TwitterStreamClient twitterStreamClient;

    private TwitterConfigurator configurator;

    private boolean startListening = false;

    private HashMap<String, TwitterMessageEvent> conversations;

    private HashMap<String, String> nodePaths;

    @Override
    public void run() {

        try {

            configurator = twitterConfiguratorFactory.getConfigurator(appConfig.getTwitterAppConfiguratorZnodePath());
            twitterStreamClient = new TwitterStreamClient(configurator);
            twitterStreamClient.initClient(this);
            conversations = new HashMap<String, TwitterMessageEvent>();
            nodePaths = new HashMap<String, String>();

            treeCache = new TreeCache(zookeeperClient, appConfig.getTwitterUsersZnodePath());
            addListener();
            treeCache.start();

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
        TreeCacheListener listener = new TreeCacheListener() {

            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {

                    case NODE_ADDED: {
                        String nodeAdded = ZKPaths.getNodeFromPath(event.getData().getPath());
                        String nodePath = event.getData().getPath();

                        log.info("Adding node:" + nodeAdded + " from path: " + event.getData().getPath());
                        /*
                         * if (event.getData() != null) { String
                         * userResolverNode =
                         * ZKPaths.getNodeFromPath(event.getData().getPath());
                         * byte[] messageBytes = event.getData().getData(); if
                         * (messageBytes != null) { TwitterMessageEvent[]
                         * twitterMessageEvents = new
                         * GsonBuilder().create().fromJson( new
                         * String(messageBytes), TwitterMessageEvent[].class);
                         * if (twitterMessageEvents != null) {
                         * addTwitterMessage(userResolverNode,
                         * twitterMessageEvents); } } } break;
                         */
                        if (event.getData().getData() != null) {
                            String nodeValue = new String(event.getData().getData());
                            log.info("Adding node value:-" + nodeValue + "-");
                            if (nodeValue.equals("NEW")) {
                                addUserResolverTrack(nodeAdded);
                            } else {
                                // new conversation
                                if (!Strings.isNullOrEmpty(nodeValue)) {
                                    byte[] messageBytes = event.getData().getData();
                                    TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                            new String(messageBytes), TwitterMessageEvent.class);
                                    if (twitterMessageEvent != null) {
                                        if (startListening) {
                                            addTwitterMessage(nodeAdded, nodePath, twitterMessageEvent);
                                        } else {
                                            checkMessageResponses(nodeAdded, nodePath, twitterMessageEvent);
                                        }
                                    }
                                } else {
                                    log.info("Not Adding node with empty value ");
                                }
                            }
                        } else {
                            log.info("Not Adding node value");
                        }
                        break;
                    }
                    case INITIALIZED: {
                        log.info("Twitter node starting to init ...");
                        ChildData initialChildData = event.getData();
                        if (initialChildData != null) {
                            log.info("Adding Init Twitter node:" + initialChildData.getPath() + " "
                                    + initialChildData.getData());
                        }
                        /*
                         * List<ChildData> initialData = initialChildData.get
                         * 
                         * if (initialData != null && initialData.size() > 0) {
                         * 
                         * List<String> userResolversFilters = new
                         * ArrayList<String>(); for (ChildData childData :
                         * initialData) { String userResolverToFilter =
                         * ZKPaths.getNodeFromPath(childData.getPath());
                         * log.info
                         * ("User Resolver - Message added at Init process: " +
                         * userResolverToFilter);
                         * conversations.put(userResolverToFilter, null);
                         * userResolversFilters.add(userResolverToFilter);
                         * 
                         * if (childData.getData() != null) { byte[]
                         * messageBytes = childData.getData();
                         * TwitterMessageEvent[] twitterMessageEvents = new
                         * GsonBuilder().create().fromJson( new
                         * String(messageBytes), TwitterMessageEvent[].class);
                         * if (twitterMessageEvents != null) {
                         * checkMessageResponses(userResolverToFilter,
                         * twitterMessageEvents); } } }
                         * twitterStreamClient.addTrackFilters
                         * (userResolversFilters); }
                         */startListening = true;
                        break;
                    }
                    case NODE_UPDATED: {
                        log.info("Twitter conversation TreeNode changed: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_REMOVED: {
                        log.info("Twitter conversation removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        treeCache.getListenable().addListener(listener);
    }

    private void addUserResolverTrack(String userResolverToFilter) {
        try {
            log.info("Adding UserResolver:" + userResolverToFilter);
            twitterStreamClient.addTrackFilter(userResolverToFilter);
        } catch (Exception e) {
            log.error("There was an error adding new User Resolver to track :" + userResolverToFilter, e);
        }
    }

    private void addTwitterMessage(String replyMessageId, String nodePath, TwitterMessageEvent twitterMessageEvent) {
        try {
            if (startListening) {
                log.info("Adding message conversation :" + replyMessageId + " from Case:"
                        + twitterMessageEvent.getCaseId());

                nodePaths.put(replyMessageId, nodePath);
                conversations.put(replyMessageId, twitterMessageEvent);
            }
        } catch (Exception e) {
            log.error("There was an error adding converstaion: " + replyMessageId, e);
        }
    }

    private void checkMessageResponses(String replyId, String nodePath, TwitterMessageEvent twitterMessageEvent) {
        TwitterClient twitterClient = new TwitterClient(this);
        twitterClient.initTwitterClient(configurator);
        nodePaths.put(replyId, nodePath);
        conversations.put(replyId, twitterMessageEvent);
        twitterClient.checkAnyMention(twitterMessageEvent);
    }

    public void stop() {
        if (twitterStreamClient != null) {
            this.twitterStreamClient.stop();
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
            log.info("Removing twitter node after detect response: " + nodePath);
            conversations.remove(conversation);
            zookeeperClient.delete().forPath(nodePath);
        } catch (Exception e) {
            log.error("Unable to remove message conversation:: " + conversation, e);
        }
    }

    @Override
    public void processEvent(Boolean isResponseFromMessage, Long timestamp, String userResolver, String[] userMentions,
            String sourceMessageId, String messageText, String inReplyToMessageId, String userId, String userName,
            String userProfileImage) {

        try {
            Event event = new Event();
            // String mainUserResolver = null;

            event.setId(sourceMessageId);
            event.setFecha(new Date());
            event.setTimestamp(timestamp);
            event.setMedioId(FilterConstants.MEDIA_TWITTER);
            event.setName(messageText);
            event.setTitulo(messageText);
            event.setTexto(messageText);
            event.setNormalizeMessage(messageText);

            if (userMentions != null) {
                for (int i = 0; i < userMentions.length; i++) {
                    String userMention = userMentions[i];
                    if (conversations.containsKey(userMention)) {
                        // mainUserResolver = userMention;
                        break;
                    }
                }
            }

            if (isResponseFromMessage) {
                TwitterMessageEvent conversationsByinReplyMessageId = conversations.get(inReplyToMessageId);
                if (conversationsByinReplyMessageId != null) {
                    if (conversationsByinReplyMessageId.getReplyMessageId().equals(inReplyToMessageId)) {
                        // is response from existing conversation
                        event.setIdPadre(conversationsByinReplyMessageId.getEventId());
                        event.setOriginIdCase(conversationsByinReplyMessageId.getCaseId());
                        removeSourceConversation(inReplyToMessageId);
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

    @Override
    public String getReplyIdToTrack(String idRootComment) {
        return null;
    }

    @Override
    public String getUserIdToTrack(String idRootComment) {
        return null;
    }
}
