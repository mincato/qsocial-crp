package com.qsocialnow.common.services.strategies;

import com.qsocialnow.common.model.responsedetector.TwitterMessageEvent;
import com.qsocialnow.common.queues.QueueConsumer;

public class TwitterMessageQueueConsumer extends QueueConsumer<TwitterMessageEvent> {

    private CheckHistoryTask<TwitterMessageEvent> sourceClient;

    public TwitterMessageQueueConsumer(String type, CheckHistoryTask<TwitterMessageEvent> sourceClient) {
        super(type);
        this.sourceClient = sourceClient;
    }

    @Override
    public void process(TwitterMessageEvent event) {
        sourceClient.checkHistory(event);
    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

}
