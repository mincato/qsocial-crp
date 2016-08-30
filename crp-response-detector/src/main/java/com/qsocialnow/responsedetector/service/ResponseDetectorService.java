package com.qsocialnow.responsedetector.service;

import java.util.List;

import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.responsedetector.strategies.ResponseDetectorStrategy;

public class ResponseDetectorService implements Runnable {

    private ResponseDetectorStrategy responseDetectorStrategy;

    private boolean stop = false;

    @Override
    public void run() {
        while (!stop) {
            List<InPutBeanDocument> events = responseDetectorStrategy.findEvents();
            // send events to kafka
        }
    }

    public void stop() {
        stop = true;
    }

    public void setResponseDetectorStrategy(ResponseDetectorStrategy responseDetectorStrategy) {
        this.responseDetectorStrategy = responseDetectorStrategy;
    }

}
