package com.qsocialnow.service.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.persistence.UserResolverRepository;
import com.qsocialnow.service.strategies.SourceStrategy;

@Component("sendResponseCaseAction")
public class SendResponseCaseAction implements Action {

    @Resource
    private Map<Long, SourceStrategy> sources;

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        String userResolverId;
        if (caseObject.getUserResolver() == null) {
            userResolverId = (String) parameters.get(ActionParameter.USER_RESOLVER);
        } else {
            userResolverId = caseObject.getUserResolver().getId();
        }
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        String postId = sources.get(userResolver.getSource()).sendResponse(caseObject, userResolver, text);
        caseObject.setPendingResponse(false);
        caseObject.setLastPostId(postId);
        caseObject.setUserResolver(new BaseUserResolver(userResolver));
        caseObject.addMessage(new Message(postId, false));
        parameters.put(ActionParameter.COMMENT, text);
        return true;
    }

}
