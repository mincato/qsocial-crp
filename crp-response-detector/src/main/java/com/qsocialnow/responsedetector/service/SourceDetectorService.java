package com.qsocialnow.responsedetector.service;

public abstract class SourceDetectorService implements Runnable {

    public abstract void stop();

    public abstract void removeSourceConversation(String messageId);

    public abstract void processEvent(Boolean isResponseFromMessage, Long time, String userResolver,
            String[] userMentions, String sourceMessageId, String messageText, String inReplyToMessageId,
            String userId, String userName, String userProfileImage);

    public abstract String getReplyIdToTrack(String idRootComment);

    public abstract String getUserIdToTrack(String idRootComment);

    public abstract void checkErrors(Exception ex);
}
