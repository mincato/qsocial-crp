package com.qsocialnow.eventresolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.processor.EventHandlerProcessor;
import com.qsocialnow.eventresolver.processor.MessageProcessor;
import com.qsocialnow.kafka.config.KafkaConfig;
import com.qsocialnow.kafka.consumer.Consumer;

@Component
public class App implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private KafkaConfig kafkaConfig;

    private ExecutorService eventHandlerExecutor;

    private ExecutorService appExecutor;

    private List<EventHandlerProcessor> eventHandlerProcessors;

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
            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);

            eventHandlerExecutor = Executors.newCachedThreadPool();
            eventHandlerProcessors = new ArrayList<>();
        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }

    }

    public void close() {
        log.info("Starting exit...");
        for (EventHandlerProcessor eventHandlerProcessor : eventHandlerProcessors) {
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
        String kafkaZookeeperHost;
        try {
            kafkaZookeeperHost = new String(zookeeperClient.getData().forPath(appConfig.getKafkaZookeeerZnodePath()));
            EventHandlerProcessor eventHandlerProcessor = new EventHandlerProcessor(new Consumer(kafkaZookeeperHost,
                    kafkaConfig, domain), messageProcessor);
            eventHandlerProcessors.add(eventHandlerProcessor);
            eventHandlerExecutor.execute(eventHandlerProcessor);
        } catch (Exception e) {
            log.error("There was an error creating the event handler processor for domain: " + domain, e);
        }

    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        log.info("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        createEventHandlerProcessor(ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case INITIALIZED: {
                        log.info("PathChildrenCache initialized");
                        List<ChildData> initialData = event.getInitialData();
                        for (ChildData childData : initialData) {
                            createEventHandlerProcessor(ZKPaths.getNodeFromPath(childData.getPath()));
                            log.info("initial node: " + ZKPaths.getNodeFromPath(childData.getPath()));
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

}
