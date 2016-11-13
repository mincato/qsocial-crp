package com.qsocialnow.common.model.responsedetector;

public class FacebookFeedEvent {

    private final String caseId;

    private final String userId;

    private final String userName;

    private final String messageId;

    private final String commentId;

    private final String rootCommentId;

    private final String userPageId;

    private final String originalId;

    public FacebookFeedEvent(final String caseId, final String userId, final String userName, final String messageId,
            final String commentId, final String rootCommentId, final String userPageId, final String originalId) {
        this.caseId = caseId;
        this.userId = userId;
        this.userName = userName;
        this.rootCommentId = rootCommentId;
        this.commentId = commentId;
        this.messageId = messageId;
        this.userPageId = userPageId;
        this.originalId = originalId;
    }

    public String getCaseId() {
        return caseId;
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

    public String getRootCommentId() {
        return rootCommentId;
    }

    public String getOriginalId() {
        return originalId;
    }

    @Override
    public int hashCode() {
        return caseId.hashCode() + userId.hashCode() + userName.hashCode() + messageId.hashCode()
                + commentId.hashCode() + rootCommentId.hashCode() + userPageId.hashCode();
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

        if (!caseId.equals(other.caseId))
            return false;

        if (!userId.equals(other.userId))
            return false;

        if (!userName.equals(other.userName))
            return false;

        if (!messageId.equals(other.messageId))
            return false;

        if (!rootCommentId.equals(other.rootCommentId))
            return false;

        if (!commentId.equals(other.commentId))
            return false;

        if (!userPageId.equals(other.userPageId))
            return false;

        if (!originalId.equals(other.originalId))
            return false;

        return true;
    }

}
