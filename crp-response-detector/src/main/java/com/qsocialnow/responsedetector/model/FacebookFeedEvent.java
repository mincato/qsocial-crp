package com.qsocialnow.responsedetector.model;

public class FacebookFeedEvent {

    private final String caseId;

    private final String eventId;

    private final String userId;

    private final String userName;

    private final String messageId;

    private final String commentId;

    private final String userPageId;

    public FacebookFeedEvent(final String caseId, final String eventId, final String userId, final String userName,
            final String replyMessageId, final String messageId, String commentId, final String userPageId) {
        this.caseId = caseId;
        this.eventId = eventId;
        this.userId = userId;
        this.userName = userName;
        this.commentId = commentId;
        this.messageId = messageId;
        this.userPageId = userPageId;
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

    public String getMessageId() {
        return messageId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPageId() {
        return userPageId;
    }

    @Override
    public int hashCode() {
        return caseId.hashCode() + eventId.hashCode() + userId.hashCode() + userName.hashCode() + messageId.hashCode()
                + commentId.hashCode() + userPageId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        FacebookFeedEvent other = (FacebookFeedEvent) obj;

        if (!eventId.equals(other.eventId))
            return false;

        if (!caseId.equals(other.caseId))
            return false;

        if (!userId.equals(other.userId))
            return false;

        if (!userName.equals(other.userName))
            return false;

        if (!messageId.equals(other.messageId))
            return false;

        if (!commentId.equals(other.commentId))
            return false;

        if (!userPageId.equals(other.userPageId))
            return false;
        return true;
    }

}
