package com.qsocialnow.responsedetector.strategies;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.event.InPutBeanDocument;

@Component
public class MockResponseDetectorStrategy implements ResponseDetectorStrategy {

    private static final Logger log = LoggerFactory.getLogger(MockResponseDetectorStrategy.class);

    @Override
    public List<InPutBeanDocument> findEvents() {
        log.info("Running mock response detector strategy...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
