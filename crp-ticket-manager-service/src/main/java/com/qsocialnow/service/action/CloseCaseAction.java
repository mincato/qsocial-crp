package com.qsocialnow.service.action;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.service.ConversationUpdater;

@Component("closeCaseAction")
public class CloseCaseAction implements Action {

    @Autowired
    private ConversationUpdater conversationUpdater;

    @Override
    public AsyncAction execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        caseObject.setCloseDate(new Date().getTime());
        caseObject.setOpen(false);
        conversationUpdater.removeConversation(caseObject);
        return null;
    }

}
