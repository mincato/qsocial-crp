package com.qsocialnow.responsedetector.service;

import com.qsocialnow.common.model.event.InPutBeanDocument;

public abstract class SourceDetectorService implements Runnable {

    public abstract void stop();

    public abstract void removeSourceConversation(String converstationPath);

    public abstract void processEvent(InPutBeanDocument event);

}
