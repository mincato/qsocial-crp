package com.qsocialnow.responsedetector.strategies;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.InPutBeanDocument;

@Component
public class TwitterResponseDetectorStrategy implements ResponseDetectorStrategy {

    private static final Logger log = LoggerFactory.getLogger(TwitterResponseDetectorStrategy.class);

    @Override
    public List<InPutBeanDocument> findEvents() {
        log.info("Running mock response detector strategy...");
        InPutBeanDocument event1 = null;
        InPutBeanDocument event2 = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("MM/dd/yyyy");
        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("eventos/evento1.json");
        event1 = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream), InPutBeanDocument.class);

        systemResourceAsStream = ClassLoader.getSystemResourceAsStream("eventos/evento2.json");
        event2 = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream), InPutBeanDocument.class);
        return Arrays.asList(event1, event2);
    }

}
