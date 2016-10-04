package com.qsocialnow.eventresolver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;
import com.qsocialnow.elasticsearch.queues.QueueService;
import com.qsocialnow.elasticsearch.queues.QueueServiceFactory;
import com.qsocialnow.elasticsearch.queues.QueueType;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.processor.EventHandlerProcessor;
import com.qsocialnow.eventresolver.processor.MessageProcessorImpl;
import com.qsocialnow.eventresolver.processor.MessageResponseDetectorProcessorImpl;
import com.qsocialnow.kafka.config.KafkaConsumerConfig;
import com.qsocialnow.kafka.consumer.Consumer;

@Component
public class App implements Runnable {

    private static final String RESPONSE_DETECTOR_CONSUMER_NAME = "RESPONSE_DETECTOR_CONSUMER";

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private KafkaConsumerConfig kafkaConfig;

    @Autowired
    private MessageProcessorImpl messageProcessor;

    @Autowired
    private MessageResponseDetectorProcessorImpl messageResponseDetectorProcessor;

    @Autowired
    private QueueConfigurator queueConfig;

    @Autowired
    private CacheManager cacheManager;

    private ExecutorService eventHandlerExecutor;

    private ExecutorService appExecutor;

    private Map<String, EventHandlerProcessor> eventHandlerProcessors;

    private PathChildrenCache pathChildrenCache;

    public static void main(String[] args) {

        try {
            ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(
                    "classpath:spring/applicationContext.xml");
            context.registerShutdownHook();
        } catch (Exception e) {
            log.error("Unexpected Error", e);
            System.exit(1);
        }

    }

    public void start() throws Exception {

        appExecutor = Executors.newSingleThreadExecutor();
        appExecutor.execute(this);

    }

    @Override
    public void run() {
        try {
            pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getDomainsPath(), true);
            addListener();
            eventHandlerExecutor = Executors.newCachedThreadPool();
            eventHandlerProcessors = new HashMap<>();
            createEventHandlerResponseDetectorProcessor();

            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }

    }

    public void close() {
        log.info("Starting exit...");
        for (EventHandlerProcessor eventHandlerProcessor : eventHandlerProcessors.values()) {
            eventHandlerProcessor.stop();
        }
        try {
            if (eventHandlerExecutor != null) {
                eventHandlerExecutor.shutdown();
                eventHandlerExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
            if (pathChildrenCache != null) {
                pathChildrenCache.close();
            }
            if (appExecutor != null) {
                appExecutor.shutdown();
                appExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    private void createEventHandlerProcessor(String domain) {
        try {
            QueueService queueService = QueueServiceFactory.getInstance().getQueueServiceInstance(QueueType.EVENTS,
                    queueConfig);

            EventHandlerProcessor eventHandlerProcessor = new EventHandlerProcessor(new Consumer(kafkaConfig, domain),
                    messageProcessor, queueService);

            eventHandlerProcessors.put(domain, eventHandlerProcessor);
            eventHandlerExecutor.execute(eventHandlerProcessor);
        } catch (Exception e) {
            log.error("There was an error creating the event handler processor for domain: " + domain, e);
        }

    }

    private void createEventHandlerResponseDetectorProcessor() {
        try {
            QueueService queueService = QueueServiceFactory.getInstance().getQueueServiceInstance(QueueType.EVENTS,
                    queueConfig);

            EventHandlerProcessor eventHandlerProcessor = new EventHandlerProcessor(new Consumer(kafkaConfig,
                    RESPONSE_DETECTOR_CONSUMER_NAME), messageResponseDetectorProcessor, queueService);

            eventHandlerProcessors.put(RESPONSE_DETECTOR_CONSUMER_NAME, eventHandlerProcessor);
            eventHandlerExecutor.execute(eventHandlerProcessor);
        } catch (Exception e) {
            log.error("There was an error creating the event handler processor for domain: "
                    + RESPONSE_DETECTOR_CONSUMER_NAME, e);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        String domain = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.info("Node added: " + domain);
                        createEventHandlerProcessor(domain);
                        break;
                    }
                    case CHILD_REMOVED: {
                        String domain = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.info("Node removed: " + domain);
                        eventHandlerProcessors.get(domain).stop();
                        eventHandlerProcessors.remove(domain);
                        break;
                    }
                    case CHILD_UPDATED: {
                        String domain = ZKPaths.getNodeFromPath(event.getData().getPath());
                        log.info("Node updated: " + domain);
                        cacheManager.getCache(CacheConfig.DOMAINS_CACHE).evict(domain);
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

}
