package com.qsocialnow.responsedetector.service;

public abstract class SourceDetectorService implements Runnable {

    public abstract void stop();

    public abstract void removeSourceConversation(String converstationPath);

}
