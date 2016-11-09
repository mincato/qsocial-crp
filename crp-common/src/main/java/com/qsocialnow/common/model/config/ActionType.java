package com.qsocialnow.common.model.config;

import java.io.Serializable;
import java.util.EnumSet;

public enum ActionType implements Serializable {

    REPLY, TAG_SUBJECT, TAG_CASE, CLOSE, REOPEN, REGISTER_COMMENT, OPEN_CASE, MERGE_CASE, SEND_MESSAGE, ASSIGN, PENDING_RESPONSE, RESOLVE, ATTACH_FILE, CHANGE_SUBJECT, CHANGE_PRIORITY;

    public static EnumSet<ActionType> automaticActions = EnumSet.of(TAG_CASE, REPLY, TAG_SUBJECT);

}
