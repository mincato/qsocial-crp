package com.qsocialnow.common.services.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.config.QueueConfigurator;
import com.qsocialnow.common.config.TwitterConfig;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.ErrorType;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.SourceCredentials;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.common.queues.QueueServiceFactory;
import com.qsocialnow.common.queues.QueueType;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSourceStrategy implements SourceStrategy, AsyncTask<SourceMessageRequest, SourceMessageResponse> {

    private static final Logger log = LoggerFactory.getLogger(TwitterSourceStrategy.class);

    private TwitterConfig twitterConfig;

    private final Map<String, QueueService> queueServices;

    private final Map<String, QueueProducer<SourceMessageRequest>> queueProducers;

    private final Map<String, QueueConsumer<SourceMessageRequest>> queueConsumers;

    private PathChildrenCache twitterUserResolverChildrenCache;

    private String twitterUsersZnodePath;

    private CuratorFramework zookeeperClient;

    private QueueConfigurator queueConfig;

    private SourceMessagePostProcess postProcess;

    public TwitterSourceStrategy() {
        queueServices = new HashMap<>();
        queueProducers = new HashMap<>();
        queueConsumers = new HashMap<>();
    }

    public void start() throws Exception {
        twitterUserResolverChildrenCache = new PathChildrenCache(zookeeperClient, twitterUsersZnodePath, true);
        addListener();
        twitterUserResolverChildrenCache.start(StartMode.NORMAL);
    }

    public void stop() {
        try {
            for (String userResolver : queueServices.keySet()) {
                this.queueConsumers.get(userResolver).stopConsumer();
                this.queueProducers.get(userResolver).stopProducer();
                this.queueServices.get(userResolver).shutdownQueueService();
            }
            if (twitterUserResolverChildrenCache != null) {
                twitterUserResolverChildrenCache.close();
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        String userResolver = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.info("User resolver added: " + userResolver);
                        initQueues(userResolver);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        twitterUserResolverChildrenCache.getListenable().addListener(listener);
    }

    @Override
    public String sendResponse(Case caseObject, UserResolver userResolver, String text, String actionRegitryId) {
        SourceMessageRequest sourceMessageRequest = new SourceMessageRequest();
        sourceMessageRequest.setSubjectIdentifier(caseObject.getSubject().getIdentifier());
        sourceMessageRequest.setText(text);
        sourceMessageRequest.setUserResolver(userResolver);
        sourceMessageRequest.setPostId(caseObject.getLastPostId());
        sourceMessageRequest.setAction(ActionType.REPLY);
        sourceMessageRequest.setCaseId(caseObject.getId());
        sourceMessageRequest.setActionRegistryId(actionRegitryId);
        queueProducers.get(userResolver.getIdentifier()).addItem(sourceMessageRequest);
        return null;
    }

    @Override
    public String sendMessage(Case caseObject, UserResolver userResolver, String text, String actionRegistryId) {
        SourceMessageRequest sourceMessageRequest = new SourceMessageRequest();
        sourceMessageRequest.setSubjectIdentifier(caseObject.getSubject().getIdentifier());
        sourceMessageRequest.setText(text);
        sourceMessageRequest.setUserResolver(userResolver);
        sourceMessageRequest.setAction(ActionType.SEND_MESSAGE);
        sourceMessageRequest.setCaseId(caseObject.getId());
        sourceMessageRequest.setActionRegistryId(actionRegistryId);
        queueProducers.get(userResolver.getIdentifier()).addItem(sourceMessageRequest);
        return null;
    }

    public SourceMessageResponse execute(SourceMessageRequest request) {
        SourceMessageResponse sourceMessageResponse = new SourceMessageResponse();
        sourceMessageResponse.setRequest(request);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey());
        configurationBuilder.setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret());
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        AccessToken accessToken = new AccessToken(request.getUserResolver().getCredentials().getToken(), request
                .getUserResolver().getCredentials().getSecretToken());
        Twitter twitter = twitterFactory.getInstance(accessToken);
        String text = request.getText();
        text = "@" + request.getSubjectIdentifier() + " " + text;
        String statusId = request.getPostId();
        try {

            StatusUpdate statusUpdate = new StatusUpdate(text);
            if (statusId != null) {
                statusUpdate.inReplyToStatusId(Long.parseLong(statusId));
            }
            Status status = twitter.updateStatus(statusUpdate);
            sourceMessageResponse.setPostId(Long.toString(status.getId()));
            return sourceMessageResponse;
        } catch (TwitterException e) {
            log.error("There was an error trying to send response via twitter", e);

            if (twitterConfig.getRetryErrorCodes().contains(e.getErrorCode())
                    || twitterConfig.getRetryStatusCodes().contains(e.getStatusCode())) {
                QueueConsumer<SourceMessageRequest> queueConsumer = queueConsumers.get(request.getUserResolver()
                        .getIdentifier());
                queueConsumer.changeInitialDelay(queueConfig.getFailDelay());
                queueProducers.get(request.getUserResolver().getIdentifier()).addItem(request);
                return null;
            } else {
                sourceMessageResponse.setErrorType(twitterConfig.getErrorMapping().getOrDefault(e.getErrorCode(),
                        ErrorType.UNKNOWN));
                sourceMessageResponse.setSourceErrorMessage(e.getMessage());
                return sourceMessageResponse;
            }
        }
    }

    private void initQueues(String userResolver) {
        if (!queueServices.containsKey(userResolver)) {
            QueueService queueService = QueueServiceFactory.getInstance().getQueueServiceInstance(
                    StringUtils.join(new String[] { QueueType.MESSAGES.type(), userResolver }, "_"), queueConfig);
            QueueProducer<SourceMessageRequest> queueProducer = new QueueProducer<SourceMessageRequest>(
                    queueService.getType());
            QueueConsumer<SourceMessageRequest> queueConsumer = new SourceMessageQueueConsumer(queueService.getType(),
                    this, postProcess);
            queueProducer.addConsumer(queueConsumer);
            queueService.startProducerConsumer(queueProducer, queueConsumer);
            queueServices.put(userResolver, queueService);
            queueConsumers.put(userResolver, queueConsumer);
            queueProducers.put(userResolver, queueProducer);
        }
    }

    public void setTwitterConfig(TwitterConfig twitterConfig) {
        this.twitterConfig = twitterConfig;
    }

    public void setTwitterUsersZnodePath(String twitterUsersZnodePath) {
        this.twitterUsersZnodePath = twitterUsersZnodePath;
    }

    public void setZookeeperClient(CuratorFramework zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public void setPostProcess(SourceMessagePostProcess postProcess) {
        this.postProcess = postProcess;
    }

    public void setQueueConfig(QueueConfigurator queueConfig) {
        this.queueConfig = queueConfig;
    }

    public static void main(String[] args) throws Exception {
        QueueConfigurator queueConfigurator = new QueueConfigurator("/tmp/bigqueue/sources/twitter/", "centaurico/",
                "centaurico/error/");
        TwitterSourceStrategy twitterSourceStrategy = new TwitterSourceStrategy();
        twitterSourceStrategy.setQueueConfig(queueConfigurator);
        try {

            TwitterConfig twitterConfig = new TwitterConfig("PsRTsDvl2oDUvjmk5Uz56uTBy",
                    "ng3xPXulo9HRJqv19eCq2qokWceaxV4aiKWZj7erWFIhGx4pvw", "");
            twitterSourceStrategy.setTwitterConfig(twitterConfig);
            twitterSourceStrategy.setTwitterUsersZnodePath("/PRC/10/twitter/users");
            CuratorFramework zookeeperClient = CuratorFrameworkFactory.newClient("54.200.37.81:2181",
                    new ExponentialBackoffRetry(1000, 3));
            zookeeperClient.start();
            twitterSourceStrategy.setZookeeperClient(zookeeperClient);
            twitterSourceStrategy.setPostProcess(new SourceMessagePostProcess() {

                @Override
                public void process(SourceMessageResponse response) {
                    log.info("process success");

                }

            });

            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey());
            configurationBuilder.setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret());

            Case caseObject = new Case();
            Subject subject = new Subject();
            subject.setIdentifier("usurioenojado");
            caseObject.setSubject(subject);
            UserResolver userResolver = new UserResolver();
            userResolver.setIdentifier("centaurico_bot3");
            SourceCredentials credentials = new SourceCredentials();
            credentials.setToken("776855442475585536-ngNF9O22Y6vzZERS8QZzC7QqgGlmXTF");
            credentials.setSecretToken("glIwmGp1fgxiA2uDMgme7ymM2bv43uwd7rHHF49tmBKhE");
            userResolver.setCredentials(credentials);
            IntStream.range(0, 1000).forEach(nbr -> {
                SourceMessageRequest sourceMessageRequest = new SourceMessageRequest();
                sourceMessageRequest.setSubjectIdentifier(caseObject.getSubject().getIdentifier());
                sourceMessageRequest.setText(RandomStringUtils.randomAlphabetic(10));
                sourceMessageRequest.setUserResolver(userResolver);
                sourceMessageRequest.setAction(ActionType.SEND_MESSAGE);
                sourceMessageRequest.setCaseId(caseObject.getId());
                twitterSourceStrategy.execute(sourceMessageRequest);
            });
        } finally {
            twitterSourceStrategy.stop();
        }
    }

}
