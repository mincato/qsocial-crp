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

import com.qsocialnow.responsedetector.service.ResponseDetectorService;
import com.qsocialnow.responsedetector.sources.TwitterClient;
import com.qsocialnow.responsedetector.sources.TwitterStatusListener;

@Component
public class App implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private ExecutorService appExecutor;

    @Resource
    private List<ResponseDetectorService> responseDetectors;

    private ExecutorService responseDetectorExecutor;

    public static void main(String[] args) {
        //ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        //context.registerShutdownHook();
    	TwitterClient client = new TwitterClient();
        client.initClient();
        client.addListeners(new TwitterStatusListener());
        client.startFiltering();
        
    }

    public void start() {
        appExecutor = Executors.newSingleThreadExecutor();
        appExecutor.execute(this);
    }

    @Override
    public void run() {
        responseDetectorExecutor = Executors.newFixedThreadPool(responseDetectors.size());
        for (Runnable responseDetector : responseDetectors) {
            responseDetectorExecutor.execute(responseDetector);
        }
    }

    public void close() {
        log.info("Starting exit...");
        for (ResponseDetectorService responseDetector : responseDetectors) {
            responseDetector.stop();
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
