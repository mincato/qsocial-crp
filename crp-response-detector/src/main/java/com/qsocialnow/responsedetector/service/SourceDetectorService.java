package com.qsocialnow.responsedetector.service;


import com.qsocialnow.responsedetector.model.TwitterMessageEvent;

import twitter4j.Status;

public abstract class SourceDetectorService implements Runnable {

    public abstract void stop();

    public abstract void removeSourceConversation(String converstationPath);

    public abstract void processEvent(TwitterMessageEvent messageEvent,Status status);

}
