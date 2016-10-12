package com.qsocialnow.common.model.config;

import java.util.EnumSet;

public enum ActionType {

    REPLY, TAG_SUBJECT, TAG_CASE, CLOSE, REOPEN, REGISTER_COMMENT, MODIFY, OPEN_CASE, MERGE_CASE, SEND_MESSAGE, ASSIGN, PENDING_RESPONSE, RESOLVE, ATTACH_FILE, CHANGE_SUBJECT;

    public static EnumSet<ActionType> automaticActions = EnumSet.of(TAG_CASE, REPLY, TAG_SUBJECT);

}
