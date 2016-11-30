package com.qsocialnow.common.services.strategies;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.event.Event;

public interface SourceStrategy {

    String sendResponse(Case caseObject, UserResolver userResolver, String text, String actionRegistryId);

    String sendMessage(Case caseObject, UserResolver userResolver, String text, String actionRegistryId);

    String getOriginalSourceId(Event event);

}
