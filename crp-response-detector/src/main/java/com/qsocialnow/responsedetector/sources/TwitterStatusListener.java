package com.qsocialnow.responsedetector.sources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.responsedetector.model.TwitterMessageEvent;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStatusListener implements StatusListener{

    private static final Logger log = LoggerFactory.getLogger(TwitterStatusListener.class);

    private TwitterClient twitterClient;
    
    private TwitterMessageEvent messageEvent;

    public TwitterStatusListener(TwitterClient twitterClient, TwitterMessageEvent messageEvent) {
		this.messageEvent = messageEvent;
		this.twitterClient = twitterClient;
	}

	
	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status status) {
		log.debug("receiving messages from "+status.getUser().getScreenName()+" - starting to creat event");
		if (String.valueOf(status.getInReplyToStatusId()).equals(this.messageEvent.getReplyMessageId())) {
			log.info("Reply detected : "+status.getId());
			generateEvent(status);
			twitterClient.removeListeners(this);
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
	 
	 private void generateEvent(Status status){
		 
	 }
}
