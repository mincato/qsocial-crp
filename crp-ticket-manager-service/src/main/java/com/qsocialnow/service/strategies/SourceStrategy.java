package com.qsocialnow.service.strategies;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;

public interface SourceStrategy {

    String sendResponse(Case caseObject, UserResolver userResolver, String text);

}
