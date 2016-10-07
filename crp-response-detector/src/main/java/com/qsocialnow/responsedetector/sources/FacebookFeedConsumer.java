package com.qsocialnow.responsedetector.sources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookFeedConsumer implements Runnable {

    private final FacebookClient facebookClient;

    private static final Logger log = LoggerFactory.getLogger(FacebookFeedConsumer.class);

    public FacebookFeedConsumer(final FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public void run() {
        facebookClient.readComments();
    }

}
