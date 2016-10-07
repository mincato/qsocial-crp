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
import com.qsocialnow.responsedetector.config.FacebookConfigurator;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.factories.FacebookConfiguratorFactory;
import com.qsocialnow.responsedetector.model.FacebookFeedEvent;
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

    @Override
    public void run() {

        try {

            configurator = facebookConfiguratorFactory.getConfigurator(appConfig.getFacebookAppConfiguratorZnodePath());

            facebookClient = new FacebookClient(configurator, this);
            facebookClient.initClient();
            facebookFeedConsumer = new FacebookFeedConsumer(facebookClient);

            conversations = new HashMap<String, FacebookFeedEvent>();
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
                        log.info("Adding node:" + nodeAdded);
                        if (event.getData().getData() != null) {
                            String nodeValue = new String(event.getData().getData());
                            log.info("Adding node value:-" + nodeValue + "-");
                            if (nodeValue.equals("NEW")) {
                                addUserResolverTrack(nodeAdded);
                            } else {
                                // new conversation
                                if (!Strings.isNullOrEmpty(nodeValue)) {
                                    byte[] messageBytes = event.getData().getData();
                                    FacebookFeedEvent facebookFeedEvent = new GsonBuilder().create().fromJson(
                                            new String(messageBytes), FacebookFeedEvent.class);
                                    if (facebookFeedEvent != null) {
                                        addFacebookFeedEvent(nodeAdded, facebookFeedEvent);
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
                    case NODE_UPDATED: {
                        log.info("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath())
                                + ", value: " + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_REMOVED: {
                        log.info("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case INITIALIZED: {
                        break;
                    }
                    default:
                        System.out.println("Other event: " + event.getType().name());
                }
            }

        };
        treeCache.getListenable().addListener(listener);
    }

    private void addUserResolverTrack(String userResolverToFilter) {
        try {
            log.info("Adding UserResolver:" + userResolverToFilter);
        } catch (Exception e) {
            log.error("There was an error adding new User Resolver to track :" + userResolverToFilter, e);
        }
    }

    private void addFacebookFeedEvent(String commentId, FacebookFeedEvent facebookFeedEvent) {
        try {

            log.info("Adding message conversation from comment :" + commentId + " from Case:"
                    + facebookFeedEvent.getCaseId());
            conversations.put(commentId, facebookFeedEvent);
            facebookClient.addNewConversation(facebookFeedEvent.getCommentId());
        } catch (Exception e) {
            log.error("There was an error adding converstaions from userResolver: " + commentId, e);
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
    public void removeSourceConversation(String userResolver, String converstation) {
        try {
            // zookeeperClient.delete().forPath(appConfig.getFacebookUsersZnodePath()
            // + "/centaurico_test/"+converstation);
        } catch (Exception e) {
            log.error("Unable to remove message conversation: " + converstation, e);
        }
    }

    private void startFeedConsumer() {
        log.info("Starting Feed Consumer to check new comments from existing posts");
        executor.scheduleWithFixedDelay(facebookFeedConsumer, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void processEvent(Boolean isResponseFromMessage, String userResolver, String[] userMentions,
            String sourceMessageId, String messageText, String inReplyToMessageId, String userId, String userName,
            String userProfileImage) {

        try {
            Event event = new Event();
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
                FacebookFeedEvent conversationsByUserResolver = conversations.get(userResolver);
                if (conversationsByUserResolver != null) {

                    /*
                     * if (twitterMessageEvent.getReplyMessageId().equals(
                     * inReplyToMessageId)) { // is response from existing
                     * conversation
                     * event.setIdPadre(twitterMessageEvent.getEventId());
                     * event.setOriginIdCase(twitterMessageEvent.getCaseId());
                     * conversationsByUserResolver.remove(twitterMessageEvent);
                     * break; }
                     */

                }
            } else {
                FacebookFeedEvent conversationsByUserResolver = conversations.get(mainUserResolver);
                if (conversationsByUserResolver != null) {
                    /*
                     * for (FacebookFeedEvent twitterMessageEvent :
                     * conversationsByUserResolver) { if
                     * (twitterMessageEvent.getUserId().equals(userId)) { // is
                     * response from existing user
                     * event.setIdPadre(twitterMessageEvent.getEventId());
                     * event.setOriginIdCase(twitterMessageEvent.getCaseId());
                     * conversationsByUserResolver.remove(twitterMessageEvent);
                     * break; } }
                     */
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
