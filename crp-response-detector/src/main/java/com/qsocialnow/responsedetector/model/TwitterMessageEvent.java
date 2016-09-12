package com.qsocialnow.responsedetector.model;

public class TwitterMessageEvent {

    private final String caseId;

    private final String eventId;

    private final String userId;

    private final String replyMessageId;

    private final String messageId;

    public TwitterMessageEvent(final String caseId, final String eventId, final String userId,
            final String replyMessageId, final String messageId) {
        this.caseId = caseId;
        this.eventId = eventId;
        this.userId = userId;
        this.replyMessageId = replyMessageId;
        this.messageId = messageId;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReplyMessageId() {
        return replyMessageId;
    }

    public String getMessageId() {
        return messageId;
    }

}
