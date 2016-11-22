package com.qsocialnow.common.services.strategies;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.queues.QueueConsumer;

public class TwitterUserToTrackQueueConsumer extends QueueConsumer<String> {

    private static final Logger log = LoggerFactory.getLogger(TwitterUserToTrackQueueConsumer.class);

    private UserToTrackTask<String> sourceClient;

    private List<String> documents = new ArrayList<String>();

    public TwitterUserToTrackQueueConsumer(String type, UserToTrackTask<String> sourceClient) {
        super(type);
        this.sourceClient = sourceClient;
    }

    @Override
    public void process(String track) {
        synchronized (documents) {
            log.debug("Adding to track new users: " + track);
            documents.add(track);
        }
    }

    @Override
    public void save() {
        synchronized (documents) {
            if (documents.size() > 0) {
                sourceClient.addNewTrackFilters(documents);
                documents.clear();
            }
        }
    }
}
