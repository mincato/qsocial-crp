package com.qsocialnow.common.model.responsedetector;

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

	@Override
	public int hashCode() {
		return caseId.hashCode() + eventId.hashCode() + userId.hashCode() + replyMessageId.hashCode()
				+ messageId.hashCode();
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

		if (!eventId.equals(other.eventId))
			return false;

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
