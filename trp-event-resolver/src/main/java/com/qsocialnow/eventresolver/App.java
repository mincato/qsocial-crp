package com.qsocialnow.eventresolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
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

@Component
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private KafkaConfig kafkaConfig;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext.xml");
        context.registerShutdownHook();

        App app = (App) context.getBean("app");
        try {
            app.start();
        } catch (Exception e) {
            log.error("Unexpected Error", e);
            System.exit(1);
        }

    }

    private void start() throws Exception {

        String kafkaZookeeperHost = new String(zookeeperClient.getData().forPath(appConfig.getKafkaZookeeerZnodePath()));
        EventHandlerProcessor eventHandlerProcessor = new EventHandlerProcessor(kafkaZookeeperHost, messageProcessor,
                kafkaConfig);
        ExecutorService eventHandlerExecutor = Executors.newSingleThreadExecutor();
        eventHandlerExecutor.execute(eventHandlerProcessor);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                log.info("Starting exit...");
                if (eventHandlerProcessor != null) {
                    eventHandlerProcessor.stop();
                }
                try {
                    if (eventHandlerExecutor != null) {
                        eventHandlerExecutor.shutdown();
                        eventHandlerExecutor.awaitTermination(10L, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    log.error("Unexpected error. Cause", e);
                }
            }
        });

    }
}
