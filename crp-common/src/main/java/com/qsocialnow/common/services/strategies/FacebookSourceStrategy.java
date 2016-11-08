package com.qsocialnow.common.services.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.config.FacebookConfig;
import com.qsocialnow.common.config.QueueConfigurator;
import com.qsocialnow.common.exception.SourceException;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.ErrorType;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.SourceCredentials;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.common.queues.QueueConsumer;
import com.qsocialnow.common.queues.QueueProducer;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.common.queues.QueueServiceFactory;
import com.qsocialnow.common.queues.QueueType;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookSourceStrategy implements SourceStrategy, AsyncTask<SourceMessageRequest, SourceMessageResponse> {

    private static final Logger log = LoggerFactory.getLogger(FacebookSourceStrategy.class);

    private FacebookConfig facebookConfig;

    private final Map<String, QueueService> queueServices;

    private final Map<String, QueueProducer<SourceMessageRequest>> queueProducers;

    private final Map<String, QueueConsumer<SourceMessageRequest>> queueConsumers;

    private CuratorFramework zookeeperClient;

    private QueueConfigurator queueConfig;

    private PathChildrenCache facebookUserResolverChildrenCache;

    private String facebookUsersZnodePath;

    private SourceMessagePostProcess postProcess;

    public FacebookSourceStrategy() {
        queueServices = new HashMap<>();
        queueProducers = new HashMap<>();
        queueConsumers = new HashMap<>();
    }

    public void start() throws Exception {
        facebookUserResolverChildrenCache = new PathChildrenCache(zookeeperClient, facebookUsersZnodePath, true);
        addListener();
        facebookUserResolverChildrenCache.start(StartMode.NORMAL);
    }

    public void stop() {
        try {
            for (String userResolver : queueServices.keySet()) {
                this.queueConsumers.get(userResolver).stopConsumer();
                this.queueProducers.get(userResolver).stopProducer();
                this.queueServices.get(userResolver).shutdownQueueService();
            }
            if (facebookUserResolverChildrenCache != null) {
                facebookUserResolverChildrenCache.close();
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
        facebookUserResolverChildrenCache.getListenable().addListener(listener);
    }

    @Override
    public String sendResponse(Case caseObject, UserResolver userResolver, String text, String actionRegistryId) {
        SourceMessageRequest sourceMessageRequest = new SourceMessageRequest();
        sourceMessageRequest.setSubjectIdentifier(caseObject.getSubject().getIdentifier());
        sourceMessageRequest.setText(text);
        sourceMessageRequest.setUserResolver(userResolver);
        sourceMessageRequest.setPostId(caseObject.getLastPostId());
        sourceMessageRequest.setAction(ActionType.REPLY);
        sourceMessageRequest.setCaseId(caseObject.getId());
        sourceMessageRequest.setActionRegistryId(actionRegistryId);
        if (caseObject.getTriggerEvent() != null) {
            sourceMessageRequest.setIdOriginal(caseObject.getTriggerEvent().getIdOriginal());
        }
        queueProducers.get(userResolver.getIdentifier()).addItem(sourceMessageRequest);
        return null;
    }

    @Override
    public String sendMessage(Case caseObject, UserResolver userResolver, String text, String actionRegistryId) {
        SourceMessageRequest sourceMessageRequest = new SourceMessageRequest();
        sourceMessageRequest.setSubjectIdentifier(caseObject.getSubject().getIdentifier());
        sourceMessageRequest.setText(text);
        sourceMessageRequest.setUserResolver(userResolver);
        sourceMessageRequest.setPostId(caseObject.getLastPostId());
        sourceMessageRequest.setAction(ActionType.SEND_MESSAGE);
        sourceMessageRequest.setCaseId(caseObject.getId());
        sourceMessageRequest.setActionRegistryId(actionRegistryId);
        if (caseObject.getTriggerEvent() != null) {
            sourceMessageRequest.setIdOriginal(caseObject.getTriggerEvent().getIdOriginal());
        }
        queueProducers.get(userResolver.getIdentifier()).addItem(sourceMessageRequest);
        return null;
    }

    public SourceMessageResponse execute(SourceMessageRequest request) {
        SourceMessageResponse sourceMessageResponse = new SourceMessageResponse();
        sourceMessageResponse.setRequest(request);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthAppId(facebookConfig.getOAuthAppId());
        configurationBuilder.setOAuthAppSecret(facebookConfig.getOAuthAppSecret());
        FacebookFactory facebookFactory = new FacebookFactory(configurationBuilder.build());
        AccessToken accessToken = new AccessToken(request.getUserResolver().getCredentials().getToken());
        Facebook facebook = facebookFactory.getInstance(accessToken);
        try {
            if (request.getPostId() == null) {
                throw new SourceException("The case does not have a post to reply");
            } else {
                String id;
                if (request.getIdOriginal() != null && request.getIdOriginal().equals(request.getPostId())) {
                    id = request.getPostId();
                } else {
                    Reading reading = new Reading();
                    reading.addParameter("fields", "parent");
                    Comment comment = facebook.getComment(request.getPostId(), reading);
                    comment = comment.getParent();

                    log.debug("Reading comment in facebook strategy:" + comment);

                    if (comment != null) {
                        id = comment.getId();
                    } else {
                        id = request.getPostId();
                    }
                }
                String commentPost = facebook.commentPost(id, request.getText());
                sourceMessageResponse.setPostId(commentPost);
                return sourceMessageResponse;
            }
        } catch (FacebookException e) {
            log.error("There was an error trying to send response via facebook", e);
            if (facebookConfig.getRetryErrorCodes().contains(e.getErrorCode())
                    || facebookConfig.getRetryStatusCodes().contains(e.getStatusCode())) {
                QueueConsumer<SourceMessageRequest> queueConsumer = queueConsumers.get(request.getUserResolver()
                        .getIdentifier());
                queueConsumer.changeInitialDelay(queueConfig.getFailDelay());
                queueProducers.get(request.getUserResolver().getIdentifier()).addItem(request);
                return null;
            } else {
                sourceMessageResponse.setErrorType(facebookConfig.getErrorMapping().getOrDefault(e.getErrorCode(),
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

    public void setFacebookConfig(FacebookConfig facebookConfig) {
        this.facebookConfig = facebookConfig;
    }

    public void setFacebookUsersZnodePath(String facebookUsersZnodePath) {
        this.facebookUsersZnodePath = facebookUsersZnodePath;
    }

    public void setPostProcess(SourceMessagePostProcess postProcess) {
        this.postProcess = postProcess;
    }

    public void setQueueConfig(QueueConfigurator queueConfig) {
        this.queueConfig = queueConfig;
    }

    public void setZookeeperClient(CuratorFramework zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public static void main(String[] args) throws InterruptedException {
        FacebookConfig facebookConfig = new FacebookConfig("203868393359312", "222526b3c76c56db5ccd5daa20b5f07b", "",
                "", "");
        FacebookSourceStrategy facebookSourceStrategy = new FacebookSourceStrategy();
        facebookSourceStrategy.setFacebookConfig(facebookConfig);

        Case caseObject = new Case();
        Event triggerEvent = new Event();
        triggerEvent.setIdOriginal("1643796052578463_1645858559038879");
        caseObject.setTriggerEvent(triggerEvent);
        caseObject.setLastPostId("1643796052578463_1645858559038879");
        Subject subject = new Subject();
        subject.setIdentifier("usurioenojado");
        caseObject.setSubject(subject);
        UserResolver userResolver = new UserResolver();
        SourceCredentials credentials = new SourceCredentials();
        credentials
                .setToken("EAAC5as8qx9ABAD1zA0oj4pZC6A7baNcFgF5c1GfTLb2m4t9DdCwNtMvHbXl1tuxnnZBe4uEAVZBCoZCylO2NrV8uKTEZAbCWedFJPHGI2TVv6qV6K5KefSDKyGCRUN1KvmMEAV6DsRbvhFOCHB5pSNsAZAcRuEhgLKduahXavzDQZDZD");
        userResolver.setCredentials(credentials);
        IntStream.range(0, 10000).forEach(nbr -> {
            System.out.println(facebookSourceStrategy.sendMessage(caseObject, userResolver, "hola " + nbr, null));
        });
    }

}
