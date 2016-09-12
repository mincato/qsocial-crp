package com.qsocialnow.responsedetector;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.qsocialnow.responsedetector.service.SourceDetectorService;

@Component
public class App implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private ExecutorService appExecutor;

    @Resource
    private List<SourceDetectorService> sourceDetectors;

    private ExecutorService responseDetectorExecutor;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext.xml");
        context.registerShutdownHook();
    }

    public void start() {
        appExecutor = Executors.newSingleThreadExecutor();
        appExecutor.execute(this);
    }

    @Override
    public void run() {
        responseDetectorExecutor = Executors.newFixedThreadPool(sourceDetectors.size());
        for (Runnable sourceDetector : sourceDetectors) {
            responseDetectorExecutor.execute(sourceDetector);
        }
    }

    public void close() {
        log.info("Starting exit...");
        if (sourceDetectors != null) {
            for (SourceDetectorService sourceDetector : sourceDetectors) {
                sourceDetector.stop();
            }
        }
        try {
            if (responseDetectorExecutor != null) {
                responseDetectorExecutor.shutdown();
                responseDetectorExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
            if (appExecutor != null) {
                appExecutor.shutdown();
                appExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }
}
