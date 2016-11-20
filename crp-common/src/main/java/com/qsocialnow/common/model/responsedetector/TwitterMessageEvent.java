package com.qsocialnow.common.model.responsedetector;

import java.io.Serializable;

public class TwitterMessageEvent implements Serializable {

    private final String caseId;

    private final String userId;

    private final String replyMessageId;

    private final String messageId;

    public TwitterMessageEvent(final String caseId, final String userId, final String replyMessageId,
            final String messageId) {
        this.caseId = caseId;
        this.userId = userId;
        this.replyMessageId = replyMessageId;
        this.messageId = messageId;
    }

    public String getCaseId() {
        return caseId;
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

    @Override
    public int hashCode() {
        return caseId.hashCode() + userId.hashCode() + replyMessageId.hashCode() + messageId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        TwitterMessageEvent other = (TwitterMessageEvent) obj;

        if (!caseId.equals(other.caseId))
            return false;

        if (!userId.equals(other.userId))
            return false;
        if (!replyMessageId.equals(other.replyMessageId))
            return false;
        if (!messageId.equals(other.messageId))
            return false;

        return true;
    }

}
