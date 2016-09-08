package com.qsocialnow.responsedetector.sources;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.service.EventProcessor;
import com.qsocialnow.responsedetector.service.SourceDetectorService;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStatusListener implements StatusListener {

    private static final Logger log = LoggerFactory.getLogger(TwitterStatusListener.class);

    private TwitterStreamClient twitterClient;

    private TwitterMessageEvent messageEvent;

    private SourceDetectorService sourceService;

    public TwitterStatusListener(SourceDetectorService sourceService, TwitterStreamClient twitterClient,
            TwitterMessageEvent messageEvent) {
        this.sourceService = sourceService;
        this.messageEvent = messageEvent;
        this.twitterClient = twitterClient;
    }

    @Override
    public void onException(Exception ex) {

    }

    @Override
    public void onStatus(Status status) {
        log.info("receiving message from " + status.getUser().getScreenName());
        if (String.valueOf(status.getInReplyToStatusId()).equals(this.messageEvent.getReplyMessageId())) {

            twitterClient.removeListeners(this);
            log.info("Reply detected : " + status.getId() + " Text: " + status.getText());
            generateEvent(status);
            this.sourceService.removeSourceConversation(this.messageEvent.getMessageId());
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    private void generateEvent(Status status) {
        InPutBeanDocument event = new InPutBeanDocument();
        event.setId(this.messageEvent.getEventId());
        event.setTexto(status.getText());
        event.setFechaCreacion(new Date());
        event.setResponseDetected(true);
        sourceService.processEvent(event);
        log.info("Creating event to handle automatic response detection");
    }
}
