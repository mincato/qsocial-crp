package com.qsocialnow.responsedetector.sources;

public class FacebookFeedConsumer implements Runnable {

    private final FacebookClient facebookClient;

    public FacebookFeedConsumer(final FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public void run() {
        facebookClient.readComments();
    }

}
