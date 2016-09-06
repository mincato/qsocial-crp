package com.qsocialnow.responsedetector.service;

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
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.factories.TwitterConfiguratorFactory;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.sources.TwitterStreamClient;
import com.qsocialnow.responsedetector.sources.TwitterClient;
import com.qsocialnow.responsedetector.sources.TwitterStatusListener;

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

    @Override
    public void run() {

        try {
            configurator = twitterConfiguratorFactory.getConfigurator(appConfig.getTwitterAppConfiguratorZnodePath());
            twitterStreamClient = new TwitterStreamClient(configurator);
            twitterStreamClient.initClient();
            twitterStreamClient.start();

            pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getTwitterMessagesPath(), true);
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
                        log.info("Message added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        byte[] messageBytes = event.getData().getData();
                        TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                new String(messageBytes), TwitterMessageEvent.class);
                        createMessageHandlerProcessor(twitterMessageEvent);
                        break;
                    }
                    case INITIALIZED: {
                        log.info("PathChildrenCache initialized");
                        List<ChildData> initialData = event.getInitialData();
                        for (ChildData childData : initialData) {
                            byte[] messageBytes = childData.getData();
                            TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                    new String(messageBytes), TwitterMessageEvent.class);
                            checkMessageResponses(twitterMessageEvent);
                            log.info("initial node-message: " + twitterMessageEvent.getMessageId());
                        }
                        startListening = true;
                        break;
                    }
                    case CHILD_UPDATED: {
                        log.info("Node-Message updated: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        byte[] messageBytes = event.getData().getData();
                        TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                new String(messageBytes), TwitterMessageEvent.class);
                        createMessageHandlerProcessor(twitterMessageEvent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

    private void createMessageHandlerProcessor(TwitterMessageEvent message) {
        try {
            if (startListening) {
                log.info("Adding message:" + message.getMessageId() + "from Case:" + message.getCaseId());
                twitterStreamClient.addListeners(new TwitterStatusListener(this, twitterStreamClient,
                        this.eventProcessor, message));
            }
        } catch (Exception e) {
            log.error("There was an error creating the message handler processor for tweet: " + message.getMessageId(),
                    e);
        }
    }

    private void checkMessageResponses(TwitterMessageEvent message) {
        TwitterClient twitterClient = new TwitterClient(eventProcessor);
        twitterClient.initTwitterClient(configurator);
        twitterClient.checkAnyMention(message);
    }

    public void stop() {
        this.twitterStreamClient.stop();
    }

    @Override
    public void removeSourceConversation(String converstation) {
        this.pathChildrenCache.clearDataBytes(appConfig.getTwitterAppConfiguratorZnodePath() + "/" + converstation);
    }
}
