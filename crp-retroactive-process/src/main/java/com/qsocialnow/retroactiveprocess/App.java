package com.qsocialnow.retroactiveprocess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import com.qsocialnow.common.model.retroactive.RetroactiveProcessEvent;
import com.qsocialnow.retroactiveprocess.config.RetroactiveProcessConfig;
import com.qsocialnow.retroactiveprocess.processors.RetroactiveProcessor;

public class App implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final String DEFAULT_ENVIRONMENT = "development";

    private ExecutorService appExecutor;

    private ExecutorService processorExecutor;

    @Autowired
    private RetroactiveProcessConfig appConfig;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private RetroactiveProcessor retroactiveProcessor;

    private NodeCache nodeCache;

    public static void main(String[] args) {
        try {
            String env = System.getenv("environment");
            if (env == null) {
                env = DEFAULT_ENVIRONMENT;
            }
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, env);
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
        processorExecutor = Executors.newSingleThreadExecutor();
        processorExecutor.execute(retroactiveProcessor);

    }

    @Override
    public void run() {
        try {

            nodeCache = new NodeCache(zookeeperClient, appConfig.getProcessZnodePath());
            addListener();
            nodeCache.start(true);
            byte[] statusNode = nodeCache.getCurrentData().getData();
            if (statusNode != null) {
                RetroactiveProcessEvent processEvent = RetroactiveProcessEvent.valueOf(new String(statusNode));
                process(processEvent, true);
            }
        } catch (Exception e) {
            log.error("There was an error getting process status", e);
        }
    }

    private void process(RetroactiveProcessEvent processEvent, boolean tryToResume) {
        switch (processEvent) {
            case START:
                if (tryToResume) {
                    retroactiveProcessor.resume();
                } else {
                    retroactiveProcessor.start();
                }
                break;
            case STOP:
                log.info("stoping process");
                retroactiveProcessor.stop();
                break;
            default:
                break;
        }

    }

    public void close() {
        log.info("Starting exit...");
        try {
            if (nodeCache != null) {
                nodeCache.close();
            }
            if (retroactiveProcessor != null) {
                retroactiveProcessor.shutdown();
            }
            if (processorExecutor != null) {
                processorExecutor.shutdown();
                processorExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
            if (appExecutor != null) {
                appExecutor.shutdown();
                appExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    public void addListener() throws Exception {
        NodeCacheListener listener = new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                log.info("The status has changed");
                try {
                    byte[] statusNode = nodeCache.getCurrentData().getData();
                    if (statusNode != null) {
                        RetroactiveProcessEvent processEvent = RetroactiveProcessEvent.valueOf(new String(statusNode));
                        process(processEvent, false);
                    }
                } catch (Exception e) {
                    log.error("There was an error getting process event", e);
                }

            }
        };
        nodeCache.getListenable().addListener(listener);
    }
}
