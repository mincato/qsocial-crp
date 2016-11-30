package com.qsocialnow.responsedetector.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.gson.Gson;
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

    private PathChildrenCache pathChildrenCache;

    @Autowired
    private FacebookConfiguratorFactory facebookConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private FacebookClient facebookClient;

    private FacebookConfigurator configurator;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private FacebookFeedConsumer facebookFeedConsumer;

    private HashMap<String, List<FacebookFeedEvent>> conversations;

    @Override
    public void run() {

        try {

            configurator = facebookConfiguratorFactory.getConfigurator(appConfig.getFacebookAppConfiguratorZnodePath());

            facebookClient = new FacebookClient(configurator, this);
            facebookClient.initClient();
            facebookFeedConsumer = new FacebookFeedConsumer(facebookClient);

            conversations = new HashMap<String, List<FacebookFeedEvent>>();
            pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getFacebookConversationsZnodePath(),
                    true);

            addListener();
            pathChildrenCache.start(); // start(StartMode.POST_INITIALIZED_EVENT);
            startFeedConsumer();

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        String nodeAdded = ZKPaths.getNodeFromPath(event.getData().getPath());

                        log.debug("Adding node:" + nodeAdded + " from path: " + event.getData().getPath());
                        if (event.getData().getData() != null) {
                            String nodeValue = new String(event.getData().getData());
                            log.debug("Adding node value:-" + nodeValue + "-");
                            if (!Strings.isNullOrEmpty(nodeValue)) {
                                byte[] messageBytes = event.getData().getData();
                                FacebookFeedEvent facebookFeedEvent = new GsonBuilder().create().fromJson(
                                        new String(messageBytes), FacebookFeedEvent.class);
                                if (facebookFeedEvent != null) {
                                    addFacebookFeedEvent(nodeAdded, facebookFeedEvent);
                                }
                            } else {
                                log.debug("Not Adding node with empty value ");
                            }
                        } else {
                            log.debug("Not Adding node value");
                        }
                        break;
                    }
                    case CHILD_UPDATED: {
                        String nodeUpdated = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.debug("Facebook conversation ChildNode changed: " + nodeUpdated + ", value: "
                                + new String(event.getData().getData()));
                        if (event.getData().getData() != null) {
                            String nodeValue = new String(event.getData().getData());
                            log.debug("Adding node value:-" + nodeValue + "-");
                            if (!Strings.isNullOrEmpty(nodeValue)) {
                                byte[] messageBytes = event.getData().getData();
                                FacebookFeedEvent facebookFeedEvent = new GsonBuilder().create().fromJson(
                                        new String(messageBytes), FacebookFeedEvent.class);
                                if (facebookFeedEvent != null) {
                                    updateFacebookFeedEvent(nodeUpdated, facebookFeedEvent);
                                }
                            } else {
                                log.debug("Not Adding node with empty value ");
                            }
                        } else {
                            log.debug("Not Adding node value");
                        }
                        break;
                    }
                    case CHILD_REMOVED: {
                        String nodeRemoved = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.debug("Facebook conversation removed: " + nodeRemoved);
                        removeFacebookFeedEvent(nodeRemoved);
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
        pathChildrenCache.getListenable().addListener(listener);
    }

    private void addFacebookFeedEvent(String caseId, FacebookFeedEvent facebookFeedEvent) {
        try {

            log.debug("Adding message conversation from comment :" + facebookFeedEvent.getRootCommentId()
                    + " from Case:" + facebookFeedEvent.getCaseId());
            conversations.compute(facebookFeedEvent.getRootCommentId(),
                    (e, events) -> events == null ? new ArrayList<>() : events).add(facebookFeedEvent);
            facebookClient.addNewConversation(facebookFeedEvent.getRootCommentId());
        } catch (Exception e) {
            log.error("There was an error adding converstaions from userResolver tracking response from id: " + caseId,
                    e);
        }
    }

    private void updateFacebookFeedEvent(String nodeUpdated, FacebookFeedEvent facebookFeedEvent) {
        List<FacebookFeedEvent> events = conversations.get(facebookFeedEvent.getRootCommentId());
        for (int i = 0; i < events.size(); i++) {
            FacebookFeedEvent event = events.get(i);
            if (event.getCaseId().equals(facebookFeedEvent.getCaseId())) {
                events.set(i, facebookFeedEvent);
                break;
            }
        }
    }

    private void removeFacebookFeedEvent(String caseId) {
        for (Entry<String, List<FacebookFeedEvent>> entry : conversations.entrySet()) {
            List<FacebookFeedEvent> value = entry.getValue();
            for (Iterator<FacebookFeedEvent> iterator = value.iterator(); iterator.hasNext();) {
                FacebookFeedEvent facebookFeedEvent = iterator.next();
                if (facebookFeedEvent.getCaseId().equals(caseId)) {
                    iterator.remove();
                    if (value.isEmpty()) {
                        facebookClient.removeConversation(entry.getKey());
                    }
                    return;
                }

            }
        }

    }

    public void stop() {
        if (facebookClient != null) {
            this.facebookClient.stop();
        }
        try {
            if (pathChildrenCache != null) {
                pathChildrenCache.close();
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    private void startFeedConsumer() {
        log.debug("Starting Feed Consumer to check new comments from existing posts");
        executor.scheduleWithFixedDelay(facebookFeedConsumer, 10, 10, TimeUnit.SECONDS);
    }

    public void processEvent(Boolean isResponseFromMessage, Long timestamp, String userResolver, String[] userMentions,
            String messageId, String messageText, String commentId, int conversationIndex, String userId,
            String userName, String userProfileImage) {

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
                FacebookFeedEvent conversationsByCommentId = conversations.get(commentId).get(conversationIndex);
                if (conversationsByCommentId != null) {
                    event.setOriginIdCase(conversationsByCommentId.getCaseId());
                    event.setIdOriginal(conversationsByCommentId.getOriginalId());
                    event.setRootCommentId(conversationsByCommentId.getRootCommentId());
                }
                updateSourceConversation(commentId, conversationIndex, messageId);
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

    private void updateSourceConversation(String rootCommentId, int conversationIndex, String commentId) {
        FacebookFeedEvent facebookFeedEvent = conversations.get(rootCommentId).get(conversationIndex);
        facebookFeedEvent.setCommentId(commentId);
        String facebookEvent = new Gson().toJson(facebookFeedEvent);
        try {
            zookeeperClient.setData().forPath(
                    appConfig.getFacebookConversationsZnodePath() + "/" + facebookFeedEvent.getCaseId(),
                    facebookEvent.getBytes());
        } catch (Exception e) {
            log.error("There was an error updating facebook event on zookeeper", e);
        }

    }

    public List<String> getReplyIdsToTrack(String commentId) {
        List<FacebookFeedEvent> conversationsByCommentId = conversations.get(commentId);
        return conversationsByCommentId != null ? conversationsByCommentId.stream()
                .map(FacebookFeedEvent::getCommentId).collect(Collectors.toList()) : new ArrayList<>();
    }

    public List<String> getUserIdsToTrack(String commentId) {
        List<FacebookFeedEvent> conversationsByCommentId = conversations.get(commentId);
        return conversationsByCommentId != null ? conversationsByCommentId.stream().map(FacebookFeedEvent::getUserId)
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
