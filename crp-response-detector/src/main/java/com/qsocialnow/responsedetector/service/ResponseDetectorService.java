package com.qsocialnow.responsedetector.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.responsedetector.strategies.ResponseDetectorStrategy;

public class ResponseDetectorService implements Runnable {

    private ResponseDetectorStrategy responseDetectorStrategy;

    @Autowired
    private EventProcessor eventProcessor;

    private boolean stop = false;

    @Override
    public void run() {
        while (!stop) {
            try {
                List<Event> events = responseDetectorStrategy.findEvents();
                eventProcessor.process(events);
                Thread.sleep(600000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        stop = true;
    }

    public void setResponseDetectorStrategy(ResponseDetectorStrategy responseDetectorStrategy) {
        this.responseDetectorStrategy = responseDetectorStrategy;
    }

}
