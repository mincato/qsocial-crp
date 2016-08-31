package com.qsocialnow.responsedetector.sources;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStatusListener implements StatusListener{

	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status status) {
		System.out.println("---------------------------------------------------------");
		System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
		System.out.println("---------------------------------------------------------");
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// TODO Auto-generated method stub
		
	}

	 @Override
     public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
         System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
	 }

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// TODO Auto-generated method stub
		
	}

	 @Override
     public void onStallWarning(StallWarning warning) {
         System.out.println("Got stall warning:" + warning);
	 }

}
