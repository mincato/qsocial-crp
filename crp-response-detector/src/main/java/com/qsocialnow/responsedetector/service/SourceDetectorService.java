package com.qsocialnow.responsedetector.service;

public abstract class SourceDetectorService implements Runnable {

    public abstract void stop();

    public abstract void removeSourceConversation(String userResolver, String messageId);

    public abstract void processEvent(Boolean isResponseFromMessage, String userResolver, String[] userMentions,
            String sourceMessageId, String messageText, String inReplyToMessageId, String userId, String userName,
            String userProfileImage);

}
